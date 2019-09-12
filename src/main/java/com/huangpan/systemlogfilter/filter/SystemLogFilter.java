package com.huangpan.systemlogfilter.filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 * @desc：
 * @title：SystemLogFilter
 * @author: huangwencai
 * @date: 2019-09-12 9:18
 * @version: v4.40.0
 */
public class SystemLogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("<==init systemLogFilter!==>");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("<==doFilter systemLogFilter!==>");
        try {
            long startTime = System.currentTimeMillis();
            MyHttpServletResponseWrapper response = new MyHttpServletResponseWrapper((HttpServletResponse) resp);
            HttpServletRequest request = null;
            //缓存流信息
            if (req instanceof HttpServletRequest) {
                request = new MyHttpServletRequestWrapper((HttpServletRequest) req);
            }
            if (null == request) {
                chain.doFilter(req, response);
            } else {
                chain.doFilter(request, response);
            }
            byte[] data = response.getCopy();
            String responseParams = new String(data, response.getCharacterEncoding());
            long endTime = System.currentTimeMillis();
            //打印日志
            System.out.println("请求用户：{" + request.getRemoteAddr() + "}");
            System.out.println("请求路径：{" + request.getRequestURI() + "}");
            System.out.println("请求方式：{" + request.getMethod() + "}");
            System.out.println("请求参数：{" + RestUtil.getRequestParameter(request) + "}");
            String URI=request.getRequestURI();
            if(StringUtils.isNotBlank(URI)&&URI.contains("/bomp/v1/exportExcel/")){
                System.out.println("响应数据：{}");
            }else {
                System.out.println("响应数据：{" + responseParams + "}");
            }
            System.out.println("响应时间：{" + (endTime - startTime) + "}");
        } catch (Exception e) {
            System.out.println("doFilter exception:");
            return;
        }
    }

    @Override
    public void destroy() {
        System.out.println("<==destroy systemLogFilter!==>");
    }
}