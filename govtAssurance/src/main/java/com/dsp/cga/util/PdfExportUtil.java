package com.dsp.cga.util;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;


@Component
public class PdfExportUtil {
	
	private final static Map<String, String> tMap = new HashMap();
	static {
		tMap.put("CGA_PA_REF", "paRefLetter");
	}
	
	@Autowired
	ReportGeneratorUtil rptGeneratorUtil;

	public byte[] generatePdf(String abbr, Context ctx) throws Exception {

		String templateFile = tMap.get(abbr);
		if (templateFile == null || templateFile.equals("")) {
			throw new FileNotFoundException("Template for report not found");
		}

		return generatePdf(ctx, templateFile);

	}
	
	public byte[] generatePdf(String abbr, Map<String, String> reportParams) throws Exception {

		String templateFile = tMap.get(abbr);
		System.out.println("templateFile: " + templateFile);
		if (templateFile == null || templateFile.equals("")) {
			throw new FileNotFoundException("Template for report not found");
		}

		Context ctx = new Context();
		reportParams.forEach((key, value) -> {
			System.out.println(key + "/ " + value);
		});
		reportParams.forEach((key, value) -> ctx.setVariable(key, value));
		return generatePdf(ctx, templateFile);

	}
	
	public byte[] generatePdf(Context ctx, String templateFile) throws Exception {

		byte[] pdfBytes = rptGeneratorUtil.generatePdf(templateFile, ctx);
		return pdfBytes;
	}
}
