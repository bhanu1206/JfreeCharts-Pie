package com.infinite.controller;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartUtils; // Import ChartUtils
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infinite.model.Chart;
import com.infinite.repository.IChartDAO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;


@Controller
public class ChartController {
	@Autowired
	private IChartDAO chartdao;
	
	@RequestMapping("/index")
	public String indexpage()
	{
		return "index";
	}

	@RequestMapping("/piechart")
	public String displayPieChart(Model model) {
		List<Chart> data = chartdao.findAll();

		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Chart ch : data) {
			dataset.setValue(ch.getName(), ch.getSalary());
		}

		JFreeChart chart = ChartFactory.createPieChart("My Pie Chart", dataset, true, true, false);

		try {
			ByteArrayOutputStream chartImageStream = new ByteArrayOutputStream();
			ChartUtils.writeChartAsPNG(chartImageStream, chart, 400, 300);
			byte[] chartImageBytes = chartImageStream.toByteArray();
			String chartImageBase64 = Base64.getEncoder().encodeToString(chartImageBytes);
			model.addAttribute("chart", chartImageBase64);
		} catch (IOException e) {
			// Handle the exception
		}

		return "pie";
	}
	@RequestMapping("/bargraph")
	  public String displayBarChart(Model model) {
        List<Chart> data = chartdao.findAll();
 
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Chart item : data) {
            dataset.addValue(item.getSalary(), "Salaries", item.getName());
        }
 
        JFreeChart chart = ChartFactory.createBarChart(
            "Salaries by Name",
            "Name",
            "Salary",
            dataset
        );
 
        try {
            ByteArrayOutputStream chartImageStream = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(chartImageStream, chart, 600, 400);
            byte[] chartImageBytes = chartImageStream.toByteArray();
            String chartImageBase64 = Base64.getEncoder().encodeToString(chartImageBytes);
            model.addAttribute("chart", chartImageBase64);
        } catch (IOException e) {
            // Handle the exception
        }
 
        return "barchart";
    }
	@RequestMapping("/histogram")
	 public String displayHistogram(Model model) {
        List<Chart> data = chartdao.findAll();
 
        double[] values = data.stream().mapToDouble(Chart::getSalary).toArray();
 
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Salary Histogram", values, 10);
 
        JFreeChart chart = ChartFactory.createHistogram(
            "Salary Histogram",
            "Salary",
            "Frequency",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            false,
            false
        );
 
        try {
            ByteArrayOutputStream chartImageStream = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(chartImageStream, chart, 600, 400);
            byte[] chartImageBytes = chartImageStream.toByteArray();
            String chartImageBase64 = Base64.getEncoder().encodeToString(chartImageBytes);
            model.addAttribute("chart", chartImageBase64);
        } catch (IOException e) {
            // Handle the exception
        }
 
        return "histogram";
    }
}
