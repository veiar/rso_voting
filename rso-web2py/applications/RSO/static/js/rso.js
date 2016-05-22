$(document).ready(function() {showPieChart([1, 2], ['a', 'b'])});

function showColumnChart(data, labels)
{
    var ctx = document.getElementById("columnChart");
    var columnChart = new Chart(ctx, {
        type: 'bar',
        backgroundColor: "rgba(255,99,132,0.2)",
        borderColor: "rgba(255,99,132,1)",
        borderWidth: 1,
        hoverBackgroundColor: "rgba(255,99,132,0.4)",
        hoverBorderColor: "rgba(255,99,132,1)",
        data: {
            labels: labels,
            datasets: [{
                label: '# głosów',
                data: data
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

function showPieChart(data, labels)
{
    var ctx = document.getElementById("pieChart");
    //var chart = new CanvasJS.Chart("pieChart",
    var pieChart = new Chart(ctx,{
        type: 'pie',
        data: data = {
            labels: labels,
            datasets: [
                {
                    data: data,
                    backgroundColor: [
                        "#FF6384",
                        "#36A2EB",
                        "#FFCE56"
                    ],
                    hoverBackgroundColor: [
                        "#FF6384",
                        "#36A2EB",
                        "#FFCE56"
                    ]
                }]
        },
        options: {
            elements: {
                arc: {
                    borderColor: "#000000"
                }
            }
        }
	});
	pieChart.render();
}
