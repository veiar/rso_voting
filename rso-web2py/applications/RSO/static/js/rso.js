$(document).ready(function() {hidePartyBlock()});

function getFrequency() {
    hidePartyBlock();
    removeCurrentChart();
    $.ajax({
        url: "/RSO/default/getPartyPercentageData.json",
        success: function(data) {
            showPieChart(data.votes, data.labels)
        },
        error: function(data) {}
    })
}

function getPartyPercentage() {
    hidePartyBlock();
    removeCurrentChart();
    $.ajax({
        url: "/RSO/default/getPartyPercentageData.json",
        success: function(data) {
            showPieChart(data.votes, data.labels)
        },
        error: function(data) {}
    })
}

function getConstituencies() {
    removeCurrentChart();
    $.ajax({
        url: "/RSO/default/getPartyPercentageData.json",
        success: function(data) {
            showPieChart(data.votes, data.labels)
        },
        error: function(data) {}
    })
}

function getCandidates() {
    showPartyBlock();
}

function getCandidatesFromParty(item){
    removeCurrentChart();
    $.ajax({
        url: "/RSO/default/getCandidatePercentageData.json",
        data: {partyId:item},
        success: function(data) {       
            showPieChart(data.votes, data.labels)
        },
        error: function(data) {debugger}
})
}

function getAge() {
    removeCurrentChart();
    $.ajax({
        url: "/RSO/default/getPartyPercentageData.json",
        success: function(data) {
            showPieChart(data.votes, data.labels)
        },
        error: function(data) {}
    })
}

function getEducation() {
    removeCurrentChart();
    $.ajax({
        url: "/RSO/default/getPartyPercentageData.json",
        success: function(data) {
            showPieChart(data.votes, data.labels)
        },
        error: function(data) {}
    })
}

function getSex() {
    removeCurrentChart();
    $.ajax({
        url: "/RSO/default/getPartyPercentageData.json",
        success: function(data) {
            showPieChart(data.votes, data.labels)
        },
        error: function(data) {}
    })
}

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
    if(data.length != 0 && labels.length != 0)
    {
        var ctx = document.getElementById("pieChart");
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
        window.currentChart = pieChart;
    }
}

function showPartyBlock()
{
    $('#partyPanel').show();
    $.ajax({
        url: "/RSO/default/getPartyList.json",
        success: function(data) {
            if(data.names.length != 0)
            {
                for(i=0; i<data.names.length; ++i)
                {
                    addPartyBlock(data.names[i], data.ids[i]);
                }
            }        
        },
        error: function(data) {}
})
}

function addPartyBlock(element, index){
    var li = document.createElement("LI");
    li.setAttribute("class", "list-group-item");    
    var div = document.createElement("DIV");
    div.setAttribute("class", "btn-group");
    div.setAttribute("style", "width: 100%"); 
    var btn = document.createElement("BUTTON");
    btn.setAttribute("class", "btn btn-info");   
    btn.setAttribute("style", "width: inherit");
    var text = document.createTextNode(element);
    
    btn.appendChild(text);
    //btn.setAttribute("onclick", getCandidatesFromParty
    btn.addEventListener('click', function(){
        getCandidatesFromParty(index);
    });

    div.appendChild(btn);
    li.appendChild(div);
    document.getElementById("partyBlock").appendChild(li);
}

function clearPartyBlock(){
    var partyBlock = document.getElementById("partyBlock");
    while (partyBlock.firstChild) {
        partyBlock.removeChild(partyBlock.firstChild);
    }
}

function hidePartyBlock(){
    clearPartyBlock();
    $('#partyPanel').hide();
}

function removeCurrentChart(){
    if (window.currentChart != undefined){                    
        currentChart.destroy();
    }   
}
