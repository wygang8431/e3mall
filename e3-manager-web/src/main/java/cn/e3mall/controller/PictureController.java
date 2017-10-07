package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.utils.FastDFSClient;
import cn.e3mall.utils.JsonUtils;

@Controller
public class PictureController {
	@Value("${image.server.url}")
	private String imageServerUrl;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String fileUpload(MultipartFile uploadFile){
		Map<String,Object> result = new HashMap<>();
		try {
			//1 获取文件扩展名
			String filename = uploadFile.getOriginalFilename();
			String extName = filename.substring(filename.lastIndexOf(".")+1);
			//2 创建一个fastDFS的客户端
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			//3执行上传处理
			String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			String url = imageServerUrl+path;
			result.put("error", 0);
			result.put("url", url);	
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", 1);
			result.put("message", "图片上传失败");
		}
		String jsonStr = JsonUtils.objectToJson(result);
		return jsonStr;
	}
}
