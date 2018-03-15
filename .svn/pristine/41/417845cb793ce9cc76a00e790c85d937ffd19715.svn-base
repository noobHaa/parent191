package cn.itcast.common.fdfs;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * 上传图片
 * 
 * @author LL
 *
 */
public class FastDFSUtil {
	public static String uploadPic(byte[] pic, String name, long size) {
		String path = null;
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");

		// 读取配置文件
		try {
			ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
			// 创建老大客户端tracker
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();

			StorageServer storageServer = null;
			StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

			String ext = FilenameUtils.getExtension(name);// 返回文件扩展名

			// 设置meta 其实就是对图片的普通描述 自定义描述
			NameValuePair[] meta_list = new NameValuePair[3];
			meta_list[0] = new NameValuePair("fileName", name);
			meta_list[1] = new NameValuePair("fileSize", String.valueOf(size));
			meta_list[2] = new NameValuePair("fileExt", ext);

			// group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
			path = storageClient1.upload_file1(pic, ext, meta_list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;

	}

}
