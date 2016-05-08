$(document).ready(function() {showColumnChart()});

function showColumnChart()
{
    document.getElementById("pieChart").style.visibility='hidden';
    document.getElementById("columnChart").style.visibility='visible';
    var ctx = document.getElementById("columnChart");
    var columnChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
        datasets: [{
            label: '# of Votes',
            data: [12, 19, 3, 5, 2, 3]
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});
}

function ShowPieChart()
{
    document.getElementById("columnChart").style.visibility='hidden';
    document.getElementById("pieChart").style.visibility='visible';
    var chart = new CanvasJS.Chart("pieChart",
	{
		title:{
			text: "Voting results"
		},
                animationEnabled: true,
		legend:{
			verticalAlign: "top",
			horizontalAlign: "left",
			fontSize: 20,
			fontFamily: "Helvetica"        
		},
		theme: "theme2",
		data: [
		{        
			type: "pie",       
			indexLabelFontFamily: "Garamond",       
			indexLabelFontSize: 20,
			indexLabel: "{label} {y}%",
			startAngle:-20,      
			showInLegend: true,
			toolTipContent:"{legendText} {y}%",
			dataPoints: [
				{  y: 83.24, legendText:"Red", label: "Google" },
				{  y: 8.16, legendText:"Blue", label: "Blue" },
				{  y: 4.67, legendText:"Yellow", label: "Yellow" },
				{  y: 1.67, legendText:"Green" , label: "Green"},       
				{  y: 0.98, legendText:"Purple" , label: "Purple"}
			]
		}
       ]
	});
	chart.render();
}
