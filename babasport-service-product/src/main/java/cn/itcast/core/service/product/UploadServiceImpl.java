package cn.itcast.core.service.product;

import org.springframework.stereotype.Service;

import cn.itcast.common.fdfs.FastDFSUtil;

@Service("uploadService")
public class UploadServiceImpl implements UploadService {

	public String uploadPic(String name, Long size, byte[] pic) {

		return FastDFSUtil.uploadPic(pic, name, size);
	}

}
