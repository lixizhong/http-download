
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;

public class HttpDownloadUtil {

	/**
	 * 根据浏览器UA对下载文件名进行编码
	 * @param userAgent
	 * @param filename
	 * @return
	 */
	public static String encodeFilename(String userAgent, String filename) {
		
		String downloadName = "";
		try {
			downloadName = URLEncoder.encode(filename, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
		String rtn = "filename=\"" + downloadName + "\"";
		if (StringUtils.isNotBlank(userAgent)) {
			userAgent = userAgent.toLowerCase();
			// IE浏览器，只能采用URLEncoder编码
			if (userAgent.indexOf("msie") != -1) {
				rtn = "filename=\"" + downloadName + "\"";
			}
			// Opera浏览器只能采用filename*
			else if (userAgent.indexOf("opera") != -1) {
				rtn = "filename*=UTF-8''" + downloadName;
			}
			// Safari浏览器，只能采用ISO编码的中文输出
			else if (userAgent.indexOf("safari") != -1) {
				try {
					rtn = "filename=\"" + new String(filename.getBytes("UTF-8"), "ISO8859-1") + "\"";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			// Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
			else if (userAgent.indexOf("applewebkit") != -1) {
				try {
					downloadName = MimeUtility.encodeText(filename, "UTF8", "B");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				rtn = "filename=\"" + downloadName + "\"";
			}
			// FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
			else if (userAgent.indexOf("mozilla") != -1) {
				rtn = "filename*=UTF-8''" + downloadName;
			}
		}
		
		return rtn;
	}
}
