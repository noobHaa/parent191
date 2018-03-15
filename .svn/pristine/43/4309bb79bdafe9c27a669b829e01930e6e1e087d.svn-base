package cn.itcast.common.convert;

/**
 * 去掉前后空格装换器
 * 
 * @author LL
 *
 */
public class CustomConvert implements org.springframework.core.convert.converter.Converter<String, String> {

	@Override
	public String convert(String source) {
		try {
			if (null != source) {
				// 删除字符串两端的空白
				source = source.trim();
				if (!"".equals(source)) {
					return source;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
