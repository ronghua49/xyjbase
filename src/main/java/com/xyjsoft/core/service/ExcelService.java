package com.xyjsoft.core.service;

import com.xyjsoft.core.query.QueryFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExcelService {
    String exportExcel(QueryFilter queryFilter, HttpServletRequest request,HttpServletResponse respons);
}
