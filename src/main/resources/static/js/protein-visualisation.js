/**
 * Created by demet on 1/17/2018.
 */
function visualiseProtein(data) {

    var width = 500;
    var height = 500;

    var objects = data;
    for(i=0; i<objects.length; i++){
        var curvePercentInner = objects[i].proteinGTG.peptides;
        var curvePercentOuter = objects[i].proteinJL.peptides;


        // Arc for paths surrounding the pie chart.
        var pathArc = d3.svg.arc()
            .innerRadius(width/4)
            .outerRadius(width/4+30);

        // Arc for the pie char inside the paths
        var pieArc = d3.svg.arc()
            .innerRadius(width/6)
            .outerRadius(width/6+30);

        var oneChart = d3.select("#chart").append("div")
            .attr('class', 'oneChart');


        // Appending SVG to the body
        var svg = oneChart.append("svg")
            .attr("width", "470")
            .attr("height", "400");


        var divToolTip = oneChart.append("div")
            .attr("class", "tooltipDiv");

        var span = divToolTip.append("span")
            .attr('id', function(d){ return objects[i].id; });


        // Pie char g (groups) container
        var pieChartInner = svg.append("g")
            .attr("transform", "translate(" + (width / 2  - 30)+ "," + (height / 2  -30)+ ")")
            .attr("width", width/4)
            .attr("height", height/4);

        // Pie char g (groups) container
        var pieChartOuter = svg.append("g")
            .attr("transform", "translate(" + (width / 2 - 30)+ "," + (height / 2 - 30) + ")")
            .attr("width", width/4)
            .attr("height", height/4);

        pieChartInner.append("text")
            .attr("dy", "-0.5em")
            .style("text-anchor", "middle")
            .style("font-size", "14px")
            .attr("class", "inner-circle")
            .attr("fill", "#36454f")
            .text(function(d) { return objects[i].proteinName + " protein"; });

        pieChartInner.append("text")
            .attr("dy", "1em")
            .style("text-anchor", "middle")
            .style("font-size", "14px")
            .attr("class", "inner-circle")
            .attr("fill", "#36454f")
            .text(function(d) { return objects[i].regionName.split("_").join(" "); });

        // Using pie layout to assigning values into pie chart format.
        var pieLayout = d3.layout.pie()
            .value(function(d){ return d.percentage; }).sort(null);


        // Creating the g (group) container for the pie chart arc segments
        var pieGroupInner = pieChartInner.selectAll(".arcs")
            .data(pieLayout(curvePercentInner))
            .enter().append("g")
            .attr("class", "arcs");

        console.log(pieGroupInner);

        var pieGroupOuter = pieChartOuter.selectAll(".arcs")
            .data(pieLayout(curvePercentOuter))
            .enter().append("g")
            .attr("class", "arcs");

      console.log(pieGroupOuter);

        // Appending the arc segment paths.
        pieGroupInner.append("path")
            .attr("d", pieArc)
            .attr("fill", function(d, i) { return d.data.colorCode; })
            .attr('id', function(d){ return 'inner_' + objects[i].id; })
            .on("click", function(d) {

                if(d.data.sequence != "no peptides found"){
                    var idSVG = this.id.substring(6);
                    var text = '<p> start: ' + d.data.start + '  end: ' + d.data.end +'</p>'
                      +objects[parseInt(idSVG)].proteinGTG.protein.sequence.substring(0, d.data.start)
                        + '<span style="color:' + d.data.colorCode +'" class="selectedPeptide">' + d.data.sequence
                        + '</span>' + objects[parseInt(idSVG)].proteinGTG.protein.sequence.substring(d.data.end);

                    $('#' + idSVG).html(text);
                }
            })
            .on("dblclick", function(d) {
                var idSVG = this.id.substring(6);
                $('#' + idSVG).html(" ");
            });

        // Appending the arc segment paths.
        pieGroupOuter.append("path")
            .attr("d", pathArc)
            .attr("fill", function(d, i) { return d.data.colorCode; })
            .attr('id', function(d){ return 'outer_' + objects[i].id; })
            .on("click", function(d) {

                if(d.data.sequence != "no peptides found"){
                    var idSVG = this.id.substring(6);

                    var text = '<p> start: ' + d.data.start + '  end: ' + d.data.end +'</p>'
                      + objects[parseInt(idSVG)].proteinJL.protein.sequence.substring(0, d.data.start)
                        + '<span style="color:' + d.data.colorCode +'" class="selectedPeptide">' + d.data.sequence
                        + '</span>' + objects[parseInt(idSVG)].proteinJL.protein.sequence.substring(d.data.end);

                    $('#' + idSVG).html(text);
                }
            })
            .on("dblclick", function(d) {
                var idSVG = this.id.substring(6);
                $('#' + idSVG).html(" ");
            });

      svg.append("svg:defs").append("svg:marker")
        .attr("id", "triangle")
        .attr("viewBox", "0 -5 10 10")
        .attr("refX", 0)
        .attr("refY", 0)
        .attr("markerWidth", 8)
        .attr("markerHeight", 8)
        .attr("orient", "auto")
        .append("path")
        .attr("d", "M0,0L10,-5L10,5");

      var arc = d3.svg.arc()
        .innerRadius(width/4+50)
        .outerRadius(width/4+50)
        .startAngle(0.25 * Math.PI) //converting from degs to radians
        .endAngle(0) //just radians

      // path
      svg.append("path")
        .attr("id", "arcPath")
        .attr("marker-end", "url(#triangle)")
        .attr("d", arc)
        .attr("transform", "translate(" + (width / 2 - 30)+ "," + (height / 2 - 30) + ")")
        .attr("stroke", "grey")
        .attr("stroke-width", "1.5")
        .attr("fill", "transparent")
        .attr("class", "edges");

      svg.append("text")
        .attr("x", (width / 2) + 40)
        .attr("y", 25)
        .attr("text-anchor", "middle")
        .style("font-size", "12px")
        .text("sequence starts from here");

    }


}