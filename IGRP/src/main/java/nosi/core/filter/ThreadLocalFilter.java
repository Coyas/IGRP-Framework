package nosi.core.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import nosi.core.webapp.Igrp;

/**
 * @author Marcel Iekiny 
 * 05 Oct 2018
 */
@WebFilter
public class ThreadLocalFilter implements Filter {

    public ThreadLocalFilter() {}

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		 Igrp.set();
	      try {
	    	// pass the request along the filter chain 	    	
	        chain.doFilter(request, response);
	      } finally {
	    	  Igrp.remove();
	      }
	}

	public void init(FilterConfig fConfig) throws ServletException {}

}
