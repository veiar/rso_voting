$(document).ready(function() {
    hidePartyBlock();
    hideSexBlock();
});

function getFrequency() {
    hidePartyBlock();
    hideSexBlock();
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
    hideSexBlock();
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
    hideSexBlock();
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
    hideSexBlock();
    showPartyBlock();
}

function getCandidatesFromParty(item){
    hideSexBlock();
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
    hideSexBlock();
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
    hideSexBlock();
    removeCurrentChart();
    $.ajax({
        url: "/RSO/default/getPartyPercentageData.json",
        success: function(data) {
            showPieChart(data.votes, data.labels)
        },
        error: function(data) {}
    })
}


function getSexList() {
    hidePartyBlock();
    showSexBlock();
}

function getSex(item) {
    removeCurrentChart();
    $.ajax({
        url: "/RSO/default/getSexPercentageData.json",
        data: {sexId:item},
        success: function(data) {       
            showPieChart(data.votes, data.labels)
        },
        error: function(data) {debugger}
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

function removeCurrentChart(){
    if (window.currentChart != undefined){                    
        currentChart.destroy();
    }   
}

//****** BLOCK WITH PARTY LIST ******

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

//****** BLOCK WITH SEX LIST ******
function showSexBlock()
{
    $('#sexPanel').show();
    $.ajax({
        url: "/RSO/default/getSexList.json",
        success: function(data) {
            if(data.names.length != 0)
            {
                for(i=0; i<data.names.length; ++i)
                {
                    addSexBlock(data.names[i], data.ids[i]);
                }
            }        
        },
        error: function(data) {}
})
}

function addSexBlock(element, index){
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
    btn.addEventListener('click', function(){
        getSex(index);
    });

    div.appendChild(btn);
    li.appendChild(div);
    document.getElementById("sexBlock").appendChild(li);
}

function clearSexBlock(){
    var sexBlock = document.getElementById("sexBlock");
    while (sexBlock.firstChild) {
        sexBlock.removeChild(sexBlock.firstChild);
    }
}

function hideSexBlock(){
    clearSexBlock();
    $('#sexPanel').hide();
}
