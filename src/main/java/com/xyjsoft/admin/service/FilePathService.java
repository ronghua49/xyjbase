package com.xyjsoft.admin.service;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.admin.model.FilePath;
import com.xyjsoft.core.service.CurdService;

import java.util.List;

/**
 * ---------------------------
 *  (FilePathService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-05 10:58:04
 * ---------------------------
 */
public interface FilePathService extends CurdService<FilePath>,Manager<String,FilePath>  {
    List<FilePath> findByType(String type);
}
