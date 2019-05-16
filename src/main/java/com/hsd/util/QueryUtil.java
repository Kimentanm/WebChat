package com.hsd.util;

import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;
import org.springframework.util.StringUtils;

/**
 * Created by zrkj on 2017/12/11.
 */
public class QueryUtil {

    /**
     * 模糊查询处理"%","_","\"等字符
     * @param param
     * @return
     */
    public static String replaceForLike(String param){

        if (StringUtils.isEmpty(param)) {
            return null;
        }

        param = StringEscapeUtils.unescapeJava(param);
        StringBuffer sb = new StringBuffer();
        char[] chars = param.toCharArray();
        for (char c: chars) {
            if (c == '%' || c == '_' || c == '\'') {
                sb.append("\\" + c);
                } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
