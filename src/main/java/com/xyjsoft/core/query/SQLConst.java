package com.xyjsoft.core.query;

public abstract interface SQLConst
{
  public static final String DB_ORACLE = "oracle";
  public static final String DB_MYSQL = "mysql";
  public static final String DB_SQLSERVER = "mssql";
  public static final String DB_SQLSERVER2008 = "mssql2008";
  public static final String DB_DB2 = "db2";
  public static final String DB_DERBY = "derby";
  public static final String DB_HBASE = "hbase";
  public static final String DB_HIVE = "hive";
  public static final String DB_H2 = "h2";
  public static final String DB_JTDS = "jtds";
  public static final String DB_MOCK = "mock";
  public static final String DB_HSQL = "hsql";
  public static final String DB_POSTGRESQL = "postgresql";
  public static final String DB_SYBASE = "sybase";
  public static final String DB_SQLITE = "sqlite";
  public static final String DB_MCKOI = "mckoi";
  public static final String DB_CLOUDSCAPE = "cloudscape";
  public static final String DB_INFORMIX = "informix";
  public static final String DB_TIMESTEN = "timesten";
  public static final String DB_AS400 = "as400";
  public static final String DB_SAPDB = "sapdb";
  public static final String DB_JSQLCONNECT = "JSQLConnect";
  public static final String DB_JTURBO = "JTurbo";
  public static final String DB_FIREBIRDSQL = "firebirdsql";
  public static final String DB_INTERBASE = "interbase";
  public static final String DB_POINTBASE = "pointbase";
  public static final String DB_EDBC = "edbc";
  public static final String DB_MIMER = "mimer";
  public static final String DB_DM = "dm";
  public static final String DB_INGRES = "ingres";
  public static final String PK_COLUMN_NAME = "ID";
  public static final String FK_COLUMN_NAME = "REFID";
  public static final String CUSTOMER_COLUMN_PREFIX = "F_";
  public static final String CUSTOMER_TABLE_PREFIX = "W_";
  public static final String CUSTOMER_INDEX_PREFIX = "IDX_";
  public static final String CUSTOMER_TABLE_HIS_SUFFIX = "_HISTORY";
  public static final String CUSTOMER_TABLE_COMM_PREFIX = "TT_";
  public static final String CUSTOMER_COLUMN_CURRENTUSERID = "curentUserId_";
  public static final String CUSTOMER_COLUMN_CURRENTORGID = "curentOrgId_";
  public static final String CUSTOMER_COLUMN_FLOWRUNID = "flowRunId_";
  public static final String CUSTOMER_COLUMN_DEFID = "defId_";
  public static final String CUSTOMER_COLUMN_CREATETIME = "CREATETIME";
}