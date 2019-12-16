package com.xyjsoft.admin.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.xyjsoft.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;

import com.xyjsoft.admin.model.BillsRules;
import com.xyjsoft.admin.dao.BillsRulesMapper;
import com.xyjsoft.admin.service.BillsRulesService;

import javax.annotation.Resource;

/**
 * ---------------------------
 *  (BillsRulesServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-04 16:17:23
 * ---------------------------
 */
@Service
public class BillsRulesServiceImpl extends AbstractManagerImpl<String, BillsRules> implements BillsRulesService {

	@Autowired
	private BillsRulesMapper billsRulesMapper;

	/**
	 * 保存、更新方法
	 * @param record  实体类
	 * @return int 更新、保存数量
	 * @since JDK1.8
	 * @author gsh456
	 * @date 2019/9/5 9:11
	 */
	@Override
	public int save(BillsRules record) {
		checkDateRule(record.getDateRule());
		checkOrderRule(record);
		checkTimeRule(record.getTimeRule());
		Long id = Optional.ofNullable(record.getId()).orElse(0L);
		String type = record.getDataType();
		if(StringUtils.isNotBlank(type)){
			List<BillsRules> rules = billsRulesMapper.findByType(type).stream().filter(e->!e.getId().equals(id)).collect(Collectors.toList());
			if(rules.size()>0){
				throw new RuntimeException("类型重复");
			}
		}
		if(record.getId() == null || record.getId() == 0) {
			record.setLastNum(0);
			record.setLastDate(null);
			return billsRulesMapper.add(record);
		}
		return billsRulesMapper.update(record);
	}

	@Override
	public int delete(BillsRules record) {
		return billsRulesMapper.delete(record.getId());
	}

	@Override
	public int delete(List<BillsRules> records) {
		for(BillsRules record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			billsRulesMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public BillsRules findById(Long id) {
		return billsRulesMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, billsRulesMapper);
	}
	
	@Override
	protected MyBatisDao<String, BillsRules> getDao() {
		return billsRulesMapper;
	}
	/**
	 * 根据规则生成单据
	 * @param rule 规则实体类
	 * @return java.lang.String 单据号
	 * @since JDK1.8
	 * @author gsh456
	 * @date 2019/9/5 9:06
	 */
	@Override
	public String createRuleNo(BillsRules rule) {
		Integer endRule = Optional.ofNullable(rule.getEndRule()).orElse(0);
		Date nowDate = getNowDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String format = sdf.format(nowDate);
		//如果不填默认六位
		Integer endNum = Optional.ofNullable(rule.getEndNum()).orElse(6);
		Integer endInt;
		boolean ibu= true;
		if(endRule==0){
			ibu=false;
		}
		if(endRule==1){
			//随机数
			endInt = Integer.parseInt(getRandom(endNum));
		}
		if(endRule==2){
			//如果是每日累计型
			endInt = Optional.ofNullable(rule.getLastNum()).orElse(0);
			endInt++;
			rule.setLastNum(endInt);
			billsRulesMapper.update(rule);
		}
		if(endRule==3){
			//如果是每日累计型
			if(format.equals(rule.getLastDate())){
				endInt = Optional.ofNullable(rule.getLastNum()).orElse(0);
			}else{
				rule.setLastDate(format);
				endInt=0;
			}
			endInt++;
			rule.setLastNum(endInt);
			billsRulesMapper.update(rule);
		}else{
			endInt=1;
		}
		String endStr="";
		if(ibu){
			int length = endStr.length();
			endStr = String.valueOf(endInt);
			if(length<endNum){
				//如果位数不足进行补位
				for (int i = 0; i < endNum-length; i++) {
					endStr = "0"+ endStr;
				}
			}
		}
		String dateStr="";
		if(StringUtils.isNotBlank(rule.getDateRule())){
			try {
				SimpleDateFormat sdf1 = new SimpleDateFormat(rule.getDateRule());
				sdf1.setLenient(false);
				dateStr = sdf1.format(nowDate);
			}catch (Exception e){
				e.printStackTrace();
				throw new RuntimeException("日期转换失败，请确认日期规则是否正确");
			}
		}
		String timeStr ="";
		if(StringUtils.isNotBlank(rule.getTimeRule())){
			try {
				SimpleDateFormat sdf1 = new SimpleDateFormat(rule.getTimeRule());
				sdf1.setLenient(false);
				timeStr = sdf1.format(nowDate);
			}catch (Exception e){
				e.printStackTrace();
				throw new RuntimeException("时间转换失败，请确认时间规则是否正确");
			}
		}
		String orderRule = Optional.ofNullable(rule.getOrderRule()).orElse("日期-时间-尾数");
		String[] split = orderRule.split("-");
		if(split.length!=3){
			throw new RuntimeException("数据错误");
		}
		String bac="";
		int num =0;
		List<String> str= new ArrayList<>();
		for (String s : split) {
			if("日期".equals(s)&& StringUtils.isNotBlank(dateStr)){
				str.add(dateStr);
			}
			if("时间".equals(s)&& StringUtils.isNotBlank(timeStr)){
				str.add(timeStr);
			}
			if("尾数".equals(s)&& StringUtils.isNotBlank(endStr)){
				str.add(endStr);
			}
		}
		if(str.size()==1){
			bac= str.get(0);
		}else if(str.size()==2){
			if(StringUtils.isNotBlank(rule.getTwoThree())){
				bac= str.get(0)+rule.getTwoThree()+str.get(1);
			}else{
				bac= str.get(0)+Optional.ofNullable(rule.getOneTwo()).orElse("")+str.get(1);
			}
		}else if(str.size()==3){
			bac= str.get(0)+Optional.ofNullable(rule.getOneTwo()).orElse("")+str.get(1)+Optional.ofNullable(rule.getTwoThree()).orElse("")+str.get(2);
		}
		return bac;
	}
	/**
	 * 根据规则生成单据
	 * @param id 规则主键
	 * @return java.lang.String 单据号
	 * @since JDK1.8
	 * @author gsh456
	 * @date 2019/9/5 9:06
	 */
	@Override
	public String createRuleNo(Long id) {
		BillsRules rule = billsRulesMapper.findById(id);
		if(rule==null){
            throw new RuntimeException("数据库不存在该信息");
        }
		return createRuleNo(rule);
	}
	/**
	 * 根据规则生成单据
	 * @param type 规则类型
	 * @return java.lang.String 单据号
	 * @since JDK1.8
	 * @author gsh456
	 * @date 2019/9/5 9:06
	 */
	@Override
	public String createRuleNo(String type) {
		if(StringUtils.isBlank(type)){
			return null;
		}
		List<BillsRules> rules = billsRulesMapper.findByType(type);
		if(rules.size()==0){
			throw new RuntimeException("规则不存在");
		}
		if(rules.size()>1){
			throw new RuntimeException("规则重复，请删除重复的规则");
		}
		return createRuleNo(rules.get(0));
	}
	/**
	 * 生成指定位数的随机数
	 * @param length 位数
	 * @return String 随机数
	 */
	private static String getRandom(int length){
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			val += String.valueOf(random.nextInt(10));
		}
		return val;
	}
	/**
	 * 排序规则校验
	 * @param record  单据规则
	 * @since JDK1.8
	 * @author gsh456
	 * @date 2019/9/5 9:08
	 */
	private void checkOrderRule(BillsRules record){
		if(StringUtils.isBlank(record.getOrderRule())){
			record.setOrderRule("日期-时间-尾数");
			return;
		}
        String[] split = record.getOrderRule().split("-");
        if(split.length!=3){
            throw new RuntimeException("排序字段的数量不够，请检查程序");
        }
        int num =0 ;
		for (String s : split) {
			if("日期".equals(s)){
				num++;
			}
			if("时间".equals(s)){
				num++;
			}
			if("尾数".equals(s)){
				num++;
			}
		}
		if(num!=3){
			throw new RuntimeException("格式错误");
		}
    }
    /**
     * 日期规则校验
     * @param str  日期规则
     * @since JDK1.8
     * @author gsh456
     * @date 2019/9/5 9:08
     */
	private void checkDateRule(String str){
		if(StringUtils.isBlank(str)){
			return;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(str);
			sdf.setLenient(false);
			Date nowDate = getNowDate();
			sdf.format(nowDate);
		}catch (Exception e){
			throw new RuntimeException("不符合日期格式");
		}
	}
	/**
	 * 时间规则校验
	 * @param str  时间规则
	 * @since JDK1.8
	 * @author gsh456
	 * @date 2019/9/5 9:08
	 */
	private void checkTimeRule(String str){
		if(StringUtils.isBlank(str)){
			return;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(str);
			sdf.setLenient(false);
			Date nowDate = getNowDate();
			sdf.format(nowDate);
		}catch (Exception e){
			throw new RuntimeException("不符合时间格式");
		}
	}
}
