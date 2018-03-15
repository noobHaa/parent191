package cn.itcast.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import cn.itcast.common.web.Constants;
import cn.itcast.core.service.product.UploadService;

@Controller
public class UploadController {
	@Autowired
	private UploadService uploadService;

	// 图片上传
	@RequestMapping(value = "/upload/uploadPic.do")
	public void uploadPic(@RequestParam(required = false) MultipartFile pic, HttpServletResponse response)
			throws IOException {
		String path = uploadService.uploadPic(pic.getOriginalFilename(), pic.getSize(), pic.getBytes());
		String url = Constants.IMG_URL + path;

		// 回显
		JSONObject jo = new JSONObject();
		jo.put("url", url);

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());

	}

	// 批量上传
	@RequestMapping(value = "/upload/uploadPics.do")
	public @ResponseBody List<String> uploadPics(@RequestParam(required = false) MultipartFile[] pics,
			HttpServletResponse response) throws IOException {
		List<String> urls = new ArrayList<String>();
		for (MultipartFile pic : pics) {
			String path = uploadService.uploadPic(pic.getOriginalFilename(), pic.getSize(), pic.getBytes());
			String url = Constants.IMG_URL + path;
			urls.add(url);
		}

		return urls;
	}

	// 富文本图片上传
	/*
	 * 原来的 public @ResponseBody List<String> uploadFck(@RequestParam(required =
	 * false) MultipartFile[] pics, HttpServletResponse response) throws
	 * IOException
	 */
	// 无敌版
	@RequestMapping(value = "/upload/uploadFck.do")
	public void uploadFck(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 强转 Spring支持
		MultipartRequest mr = (MultipartRequest) request;
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile pic = entry.getValue();
			String path = uploadService.uploadPic(pic.getOriginalFilename(), pic.getSize(), pic.getBytes());
			String url = Constants.IMG_URL + path;

			// 回显
			JSONObject jo = new JSONObject();
			
			//需告知页面正确
			jo.put("error", 0);
			jo.put("url", url);

			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(jo.toString());
		}

	}
}
