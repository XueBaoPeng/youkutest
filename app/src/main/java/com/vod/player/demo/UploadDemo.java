package com.vod.player.demo;


import com.example.fastlibrary.youkucloud.Module.YoukuApi;
import com.example.fastlibrary.youkucloud.Utilities.CheckTool;
import com.example.fastlibrary.youkucloud.Utilities.ErrorCode;
import com.example.fastlibrary.youkucloud.Utilities.Json.JSONObject;
import com.example.fastlibrary.youkucloud.Utilities.MD5;
import com.example.fastlibrary.youkucloud.YoukuApiModuleCenter;

import java.io.File;
import java.util.TreeMap;

public class UploadDemo{
	public static void main(String[] args) {
		TreeMap<String, Object> config = new TreeMap<String, Object>();
		config.put("AppKey", "");
		config.put("AppSecret", "");
		config.put("RequestMethod", "POST");
		try{
			YoukuApiModuleCenter module = new YoukuApiModuleCenter(new YoukuApi(config));
			System.out.println("starting...");
			System.out.println("........上传视频文件基本信息BEGIN...........");
			String fileName = "文件路径";

			TreeMap<String, Object> params = new TreeMap<String, Object>();
			File file = new File(fileName);
			if(!file.exists()||file.isDirectory()) {
				throw new Exception(ErrorCode.FILE_NOT_FOUND.getDesc());
			}
			String contentType = file.getName().substring(file.getName().lastIndexOf(".") + 1);
			if(!CheckTool.checkFileTypeIsValid(contentType)) {
				throw new Exception(ErrorCode.ILLEGAL_FILE_TYPE.getDesc());
			}
			String fileMd5 = MD5.fileNameToMD5(fileName);

			params.put("action", "youku.api.vod.upload.video");
			/**
			 * 可不填，默认md5加密
			 */
			params.put("sign_method", "md5");
			params.put("file_size", file.length());
			params.put("file_formate", contentType);
			params.put("md5", fileMd5);
			params.put("title", file.getName().substring(0, file.getName().lastIndexOf(".")));

			JSONObject result = new JSONObject(module.call("callApi", params));
			/**
			 * 返回结果示例
			 * {
			 *     "e" : {"desc":"","provider":"cloudvideo","code":0},
			 *     "data":{"fid":"56d7b7260cf2da44604bd132","token":"1456977702892huj3","upload_url":"http://101.201.172.149/cloudvideo/uploader/post.json","file_uploaded":false},
			 *     "cost":0.01900000125169754
			 * }
			 * 字段说明：
			 * e.code: 结果码, 0为成功, 非0为失败
			 * e.desc: 失败原因
			 * e.provider: 接口模块名
			 *
			 * data.fid: 文件ID, 用于文件上传服务及状态（上传、切片、转码等）查询
			 * data.token: 用于上传服务安全校验
			 * data.upload_url: 上传服务器地址
			 * data.file_uploaded: 文件是否上传过
			 */
			//返回结果中code不为0则说明获取失败
			if(result.getJSONObject("e").getInt("code") != 0) {
				//打印失败原因
				System.out.println(result.getJSONObject("e").getString("desc"));
				throw new Exception(result.toString()) ;
			}

			System.out.println("上传视频文件基本信息result..." + result);
			System.out.println(".............上传视频文件基本信息END...........");

			System.out.println("........上传视频文件BEGIN...........");
			//文件ID, 用于上传及状态查询
			String fid = result.getJSONObject("data").getString("fid");
			//上传文件所需token, 10分钟内有效
			String token = result.getJSONObject("data").getString("token");
			//上传服务地址
			String upload_url = result.getJSONObject("data").getString("upload_url");
			params = new TreeMap<String, Object>();
			params.put("file", fileName);
			params.put("id", fid);
			params.put("sign", token);
			params.put("fmd5", fileMd5);
			params.put("requestUrl", upload_url);
			result = new JSONObject(module.call("uploadVodFile", params));
			/**
			 * 返回结果示例
			 * {
			 *     "e" : {"desc":"ok","provider":"uploader","code":0}
			 * }
			 * 字段说明：
			 * e.code: 结果码, 0为成功, 非0为失败
			 * e.desc: 描述
			 * e.provider: 接口模块名
			 */
			System.out.println("上传视频文件result..." + result);
			System.out.println(".............上传视频文件END...........");

			//返回结果中code不为0则说明获取失败
			if(result.getJSONObject("e").getInt("code") != 0) {
				//打印失败原因
				System.out.println(result.getJSONObject("e").getString("desc"));
				throw new Exception(result.toString()) ;
			}

			System.out.println(".............检查视频文件状态BEGIN...........");
			result = new JSONObject();
			//每30秒获取一次转码、切片状态
			do{
				params = new TreeMap<>();
				params.put("action", "youku.api.vod.get.upload.status");
				params.put("fid", fid);
				Thread.sleep(3 * 1000);
				result = new JSONObject(module.call("callApi", params));
				System.out.println(result);
				if(result.getJSONObject("e").getInt("code") != 0) {
					break;
				}
			} while (result.getJSONObject("data").getInt("status") != 1);
			/**
			 * 返回结果示例(status不为1时，即未完成转码、切片)
			 * {
			 *     "e" : {"desc":"","provider":"cloudvideo","code":0},
			 *     "data" : {"status" : 0, "desc" : "preparing"}
			 * }
			 * 返回结果示例(status=1时，即转码、切片任务完成)
			 * {
			 *     "e" : {"desc":"","provider":"cloudvideo","code":0},
			 *     "data" : {"status" : 1, "desc" : "preparing", "vid" : "55bf483ee4b0e28aa91e1a88"}
			 * }
			 * 字段说明：
			 * e.code: 结果码, 0为查询成功, 非0为查询失败
			 * e.desc: 描述
			 * e.provider: 接口模块名
			 *
			 * data.status: 视频文件状态码『0：转码中；1：完成；101：切片失败；201：转码失败；301：分发失败；-5: 用户已删除』
			 * data.desc: 状态描述
			 * data.vid: 播放器播放视频所需id
			 */
			System.out.println("视频文件状态result..." + result);
			System.out.println(".............检查视频文件状态END...........");

			System.out.println("end...");
		}
		catch (Exception e) {
			System.out.println("error...");
		}
	}
}
