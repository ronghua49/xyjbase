package com.xyjsoft.admin.service.impl;

import java.util.*;

import io.micrometer.core.instrument.Meter;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.admin.dao.SysTreeMapper;
import com.xyjsoft.admin.model.SysTree;
import com.xyjsoft.admin.service.SysTreeService;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;

/**
 * ---------------------------
 * 分类管理表 (SysTreeServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-26 11:08:47
 * ---------------------------
 */
@Service("sysTreeService")
public class SysTreeServiceImpl extends AbstractManagerImpl<String, SysTree> implements SysTreeService {

    @Autowired
    private SysTreeMapper sysTreeMapper;

    @Override
    public int save(SysTree record) {
        String code = record.getCode();
        if (record.getId() == null || record.getId() == 0) {
        	List<SysTree> fiandByCode = sysTreeMapper.fiandByCode(code);
        	if (fiandByCode.size() == 0){
                return sysTreeMapper.add(record);
            }else{
            	throw new RuntimeException("code值已被使用");
            }
        }else {
        	List<SysTree> fiandByCodeAndNotId = sysTreeMapper.fiandByCodeAndNotId(code,record.getId());
        	if(fiandByCodeAndNotId.size() == 0) {
        		return sysTreeMapper.update(record);
        	}else{
        		throw new RuntimeException("code值已被使用");
        	}
        }
    }

    @Override
    public int delete(SysTree record) {
    	SysTree findById = sysTreeMapper.findById(record.getId());
    	if("XYJROOT".equals(findById.getCode())) {
    		throw new RuntimeException("系统分类不可删除!");
    	}
    	if("BpmTree".equals(findById.getCode())) {
    		throw new RuntimeException("系统分类不可删除!");
    	}
    	if("KeyWord".equals(findById.getCode())) {
    		throw new RuntimeException("系统分类不可删除!");
    	}
    	if("PassKeyWord".equals(findById.getCode())) {
    		throw new RuntimeException("系统分类不可删除!");
    	}
    	if("RejectKeyWord".equals(findById.getCode())) {
    		throw new RuntimeException("系统分类不可删除!");
    	}
    	if("MenuRoute".equals(findById.getCode())) {
    		throw new RuntimeException("系统分类不可删除!");
    	}
        return sysTreeMapper.delete(record.getId());
    }

    @Override
    public int delete(List<SysTree> records) {
        for (SysTree record : records) {
            delete(record);
        }
        return 1;
    }

    @Override
    public int deleteT(List<Long> records) {
        for (Long id : records) {
            List<Long> ids = deleteZi(id);
            ids.add(id);
            for (Long aLong : ids) {
                sysTreeMapper.delete(aLong);
            }
        }
        return 1;
    }

    /*
     * @Author wangzhengwen
     * @Description 获取全部下级id
     * @Date 16:01 2019/8/27 0027
     * @Param [id]
     * @return java.util.List
     **/
    private List deleteZi(Long id) {
        List<Long> lists = new ArrayList<>();
        List<SysTree> page = sysTreeMapper.findPage();
        for (SysTree sysTree : page) {
            Long cid = (long) sysTree.getCid();
            if (cid.equals(id)) {
                lists.add(sysTree.getId());
                deleteZi(sysTree.getId());
            }
        }
        return lists;
    }

    @Override
    public SysTree findById(Long id) {
        return sysTreeMapper.findById(id);
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return MybatisPageHelper.findPage(pageRequest, sysTreeMapper);
    }

    @Override
    protected MyBatisDao<String, SysTree> getDao() {
        return sysTreeMapper;
    }

    /**
     * @return java.util.List<java.lang.Object>
     * @Author wangzhengwen
     * @Description 查看全部分类管理菜单
     * @Date 18:33 2019/8/26 0026
     * @Param []
     **/
    @Override
    public List<Object> fiandAll() {
        List<SysTree> page = sysTreeMapper.findPage();
        return menuList(page);
    }

    /*
     * @Author wangzhengwen
     * @Description 本id下的所有
     * @Date 17:20 2019/8/29 0029
     * @Param [id]
     * @return java.util.List<java.lang.Object>
     **/
    @Override
    public Map<String, Object> fiandDownById(Long id) {
        SysTree byId = sysTreeMapper.findById(id);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("id", byId.getId());
        map.put("name", byId.getName());
        map.put("cid", byId.getCid());
        map.put("code", byId.getCode());
        map.put("lastUpdateBy", byId.getLastUpdateBy());
        map.put("lastUpdateTime", byId.getLastUpdateTime());
        map.put("remark", byId.getRemark());
        map.put("childList", sysTreeChile(byId.getId(), sysTreeMapper.findPage()));
        return map;
    }

    /**
     * @return java.util.Map<java.lang.String , java.lang.Object>
     * @Author wangzhengwen
     * @Description 向上查询
     * @Date 17:48 2019/8/29 0029
     * @Param [id]
     **/
    @Override
    public Map<String, Object> fiandUpById(Long id) {
        SysTree byId = sysTreeMapper.findById(id);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("id", byId.getId());
        map.put("name", byId.getName());
        map.put("cid", byId.getCid());
        map.put("code", byId.getCode());
        map.put("lastUpdateBy", byId.getLastUpdateBy());
        map.put("lastUpdateTime", byId.getLastUpdateTime());
        map.put("remark", byId.getRemark());
        map.put("childList", sysTreeChileUp(byId.getCid(), sysTreeMapper.findPage()));
        return map;
    }
    /**
     * @Author wangzhengwen
     * @Description 根据code值进行查询
     * @Date 14:37 2019/9/2 0002
     * @Param [code]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @Override
    public Map<String, Object> fiandByCode(String code) {
        List<SysTree> sysTrees = sysTreeMapper.fiandByCode(code);
        if(sysTrees.size() > 1) {
        	throw new RuntimeException("数据编号重复异常,请联系管理员处理数据!");
        }
        SysTree sysTree = sysTrees.get(0);
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("id", sysTree.getId());
        hashMap.put("name", sysTree.getName());
        hashMap.put("cid", sysTree.getCid());
        hashMap.put("code", sysTree.getCode());
        hashMap.put("lastUpdateBy", sysTree.getLastUpdateBy());
        hashMap.put("lastUpdateTime", sysTree.getLastUpdateTime());
        hashMap.put("remark", sysTree.getRemark());
        hashMap.put("childList", sysTreeChile(sysTree.getId(), sysTreeMapper.findPage()));
        return hashMap;
    }

    public static List<Object> menuList(List<SysTree> menu) {
        List<Object> list = new ArrayList<Object>();
        //便利传过来的数据
        for (SysTree sysTree : menu) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            //获取到等级最高等级的
            if (sysTree.getCid() == 0) {
                map.put("id", sysTree.getId());
                map.put("name", sysTree.getName());
                map.put("cid", sysTree.getCid());
                map.put("code", sysTree.getCode());
                map.put("lastUpdateBy", sysTree.getLastUpdateBy());
                map.put("lastUpdateTime", sysTree.getLastUpdateTime());
                map.put("remark", sysTree.getRemark());
                map.put("childList", sysTreeChile(sysTree.getId(), menu));
                list.add(map);
            }
        }
        return list;
    }

    //查询所有子类
    private static Object sysTreeChile(Long id, List<SysTree> sysTrees) {
        List<Object> lists = new ArrayList<>();
        for (SysTree sysTree : sysTrees) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            Long sysTreeCid = (long) sysTree.getCid();
            if (sysTreeCid.equals(id)) {
                map.put("id", sysTree.getId());
                map.put("name", sysTree.getName());
                map.put("cid", sysTree.getCid());
                map.put("code", sysTree.getCode());
                map.put("lastUpdateBy", sysTree.getLastUpdateBy());
                map.put("lastUpdateTime", sysTree.getLastUpdateTime());
                map.put("remark", sysTree.getRemark());
                map.put("childList", sysTreeChile(sysTree.getId(), sysTrees));
                lists.add(map);
            }
        }
        return lists;
    }

    //查询所有父类
    private Object sysTreeChileUp(Long cid, List<SysTree> page) {
        Long cid1=(long)cid;
        ArrayList<Object> list = new ArrayList<>();
        for (SysTree sysTree : page) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            Long sysTreeCid = (long) sysTree.getId();
            if (sysTreeCid.equals(cid1)) {
                map.put("id", sysTree.getId());
                map.put("name", sysTree.getName());
                map.put("cid", sysTree.getCid());
                map.put("code", sysTree.getCode());
                map.put("lastUpdateBy", sysTree.getLastUpdateBy());
                map.put("lastUpdateTime", sysTree.getLastUpdateTime());
                map.put("remark", sysTree.getRemark());
                map.put("childList", sysTreeChileUp(sysTree.getCid(), page));
                list.add(map);
            }
        }
        return list;
    }
}
