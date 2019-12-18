package com.xyjsoft.core.influx;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author sjg
 * 2019年12月13日 下午2:36:40  
 * influxDB时序数据库操作类
 */
@ApiModel(description="InfluxClient  influxDB时序数据库操作类")
public class InfluxClient {
	private static Logger logger = LoggerFactory.getLogger(InfluxClient.class); 
	@ApiModelProperty(name="influxDB",notes="客户端实例对象")
	private InfluxDB influxDB;
	/**
	 * @describe 服务器网址，例如：http://127.0.0.1:9010
	 * @author sjg 
	 * @date 2019年12月13日 下午1:31:21
	 */
	@ApiModelProperty(name="url",notes="服务器网址，例如：http://127.0.0.1:9010")
	private String url;
	/**
	 * @describe 用户名
	 * @author sjg 
	 * @date 2019年12月13日 下午1:48:05
	 */
	@ApiModelProperty(name="userName",notes="用户名")
	private String userName;
	/**
	 * @describe 密码
	 * @author sjg 
	 * @date 2019年12月13日 下午1:48:12
	 */
	@ApiModelProperty(name="password",notes="密码")
	private String password;
	/**
	 * @describe 数据库名称
	 * @author sjg 
	 * @date 2019年12月13日 下午1:48:24
	 */
	@ApiModelProperty(name="database",notes="数据库名称")
	private String database;
	
	/**
	 * @describe 异步写入数据，如果大于等于当前设置值则自动写入influxDB数据库数据
	 * @author sjg 
	 * @date 2019年12月13日 下午1:38:40
	 */
	@ApiModelProperty(name="messageNumber",notes="异步写入数据，如果大于等于当前设置值则自动写入influxDB数据库数据")
	private int messageNumber;
	/**
	 * @describe 异步写入数据，如果消息写入空闲时间大于等于当前设置值(单位：毫秒)，则自动写入influxDB数据库数据
	 * @author sjg 
	 * @date 2019年12月13日 下午1:40:15
	 */
	@ApiModelProperty(name="timeWrite",notes="异步写入数据，如果消息写入空闲时间大于等于当前设置值(单位：毫秒)，则自动写入influxDB数据库数据")
	private int timeWrite;
	
	/**
	 * @describe 数据保存策略
	 * @author sjg 
	 * @date 2019年12月13日 下午1:59:24
	 */
	@ApiModelProperty(name="timeWrite",notes="数据保存策略")
	private String retention;
	
	public InfluxClient(String url,String userName,String password,String database,
			int messageNumber,int timeWrite,String retention){
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.database = database;
		this.messageNumber = messageNumber;
		this.timeWrite = timeWrite;
		this.retention = retention;
	}
	
	/**
	 * @describe 获取连接对象
	 * @author sjg 
	 * @date 2019年12月13日 下午2:45:05
	 * @version 1.0
	 * @return
	 */
	public void connectInfluxDB() {
		influxDB = InfluxDBFactory.connect(url, userName, password);	
        try {	
            /**	
             * 异步插入：	
             * enableBatch这里第一个是point的个数，第二个是时间，单位毫秒	
             * point的个数和时间是联合使用的，如果满100条或者2000毫秒	
             * 满足任何一个条件就会发送一次写的请求。	
             */	
            influxDB.setDatabase(database)	
                    .enableBatch(messageNumber,timeWrite, TimeUnit.MILLISECONDS);	
        } catch (Exception e) {	
        	logger.error("时序数据库设置失败：" + e.getMessage());
        } finally {	
            influxDB.setRetentionPolicy(retention);	
        }	
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);	
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public int getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}

	public int getTimeWrite() {
		return timeWrite;
	}

	public void setTimeWrite(int timeWrite) {
		this.timeWrite = timeWrite;
	}

	public String getRetention() {
		return retention;
	}

	public void setRetention(String retention) {
		this.retention = retention;
	}

	/**
	 * @describe  写入时序数据库数据
	 * @author sjg 
	 * @date 2019年12月13日 下午3:19:15
	 * @version 1.0
	 * @param point
	 */
	public void write(Point point) {
		if(influxDB == null){
			connectInfluxDB();
		}
		influxDB.write(this.database,this.retention,point);
	}
}
