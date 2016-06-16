
package com.igomall.template.method;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

@Component("countdownMethod")
public class CountDownMethod implements TemplateMethodModel {

	@SuppressWarnings("rawtypes")
	public Object exec(List arguments) throws TemplateModelException {
		if (arguments != null && !arguments.isEmpty() && arguments.get(0) != null) {
			String arg = (String) arguments.get(0);
			String[] strs = arg.split(" ");
			String[] strs1 = strs[0].split("-");
			String[] strs2 = strs[1].split(":");
			DateTime dateTime = new DateTime(Integer.valueOf(strs1[0]),Integer.valueOf(strs1[1]),Integer.valueOf(strs1[2]),Integer.valueOf(strs2[0]),Integer.valueOf(strs2[1]),Integer.valueOf(strs2[2]));
			Long mus = dateTime.getMillis()/1000;
			Long mus1 = new Date().getTime()/1000;
			Long result = mus-mus1;
			return new SimpleScalar(result.toString());
		}
		return null;
	}

}