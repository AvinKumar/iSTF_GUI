
AmCharts.ready(function () {
          

      

              var chart;
                // SERIAL CHART
                chart = new AmCharts.AmSerialChart();
                chart.dataProvider = chartData;
                chart.categoryField = "feature";

                // sometimes we need to set margins manually
                // autoMargins should be set to false in order chart to use custom margin values
                chart.autoMargins = false;
                chart.marginLeft = 0;
                chart.marginRight = 0;
                chart.marginTop = 10;
                chart.marginBottom = 50;

                // AXES
                // category
                var categoryAxis = chart.categoryAxis;
                categoryAxis.gridAlpha = 0.5;
                categoryAxis.axisAlpha = 0;
                categoryAxis.gridPosition = "start";
				categoryAxis.labelRotation= 10;

                // value
                var valueAxis = new AmCharts.ValueAxis();
                valueAxis.stackType = "regular"; // this line makes the chart 100% stacked

                valueAxis.gridAlpha = 0.5;
                valueAxis.axisAlpha = 0;
                valueAxis.labelsEnabled = false;
                chart.addValueAxis(valueAxis);

                // GRAPHS
                // first graph
                var graph = new AmCharts.AmGraph();
                graph.title = "pass";
               // graph.labelText = "[[percents]]%";
                graph.labelText = "[[value]]";
                graph.balloonText = "[[title]], [[category]]<br><span style='font-size:14px;'><b>[[value]]</b> ([[percents]]%)</span>";
                graph.valueField = "pass";
                graph.type = "column";
                graph.lineAlpha = 0;
                graph.fillAlphas = 1;
                graph.lineColor = "#00CC00";
                chart.addGraph(graph);

                // second graph
                graph = new AmCharts.AmGraph();
                graph.title = "fail";
              //  graph.labelText = "[[percents]]%";
                graph.labelText = "[[value]]";
                graph.balloonText = "[[title]], [[category]]<br><span style='font-size:14px;'><b>[[value]]</b> ([[percents]]%)</span>";
                graph.valueField = "fail";
                graph.type = "column";
                graph.lineAlpha = 0;
                graph.fillAlphas = 1;
                graph.lineColor = "#FF0000";
                chart.addGraph(graph);

                // third graph
                graph = new AmCharts.AmGraph();
                graph.title = "skip";
               // graph.labelText = "[[percents]]%";
                graph.labelText = "[[value]]";
                graph.balloonText = "[[title]], [[category]]<br><span style='font-size:14px;'><b>[[value]]</b> ([[percents]]%)</span>";
                graph.valueField = "skip";
                graph.type = "column";
                graph.lineAlpha = 0;
                graph.fillAlphas = 1;
                graph.lineColor = "#FFFF00";
                chart.addGraph(graph);

                

                // LEGEND
                var legend = new AmCharts.AmLegend();
                legend.borderAlpha = 0.2;
                legend.horizontalGap = 10;
                legend.autoMargins = false;
                legend.marginLeft = 20;
                legend.marginRight = 20;

                chart.addLegend(legend);

                // WRITE
                chart.write("chartdiv");
				chart.depth3D = 25;
                    chart.angle = 70;
               
					chart.validateNow();
            });

            // this method makes chart 2D/3D
      