$(document).ready(function() {
    showCandidatesPanel();
   
});

function init(){
    
    $('.charts').show();
    $('#candidates').hide();
    $('#chooseParameterMessage').hide();
    hidePartyBlock();
    hideSexBlock();
    removeCurrentChart();
}

function getFrequency() {
    init();
    $('.col-md-9').css('width','100%');
    $.ajax({
        url: "/RSO/default/getFrequency.json",
        success: function(data) {
            showPieChart([data, 100 - data], ["Głosujących", "Niegłosujących"]);
        },
        error: function(data) {debugger}
    })
}

function getPartyPercentage() {
    init();
    $('.col-md-9').css('width','100%');
    $.ajax({
        url: "/RSO/default/getPartyPercentageData.json",
        success: function(data) {
            showPieChart(data.votes, data.labels);
        },
        error: function(data) {}
    })
}

function getConstituencies() {
    init();
    $('.col-md-9').css('width','100%');
    $.ajax({
        url: "/RSO/default/getConstituenciesPercentageData.json",
        success: function(data) {
            showMixedColumnChart(data.votes, data.districts)
        },
        error: function(data) {}
    })
}

function getCandidates() {
    init();
    $('.col-md-9').css('width','75%');
    showPartyBlock();
}

function getAge() {
    init();
    $('.col-md-9').css('width','100%');
    $.ajax({
        url: "/RSO/default/getAgePercentageData.json",
        success: function(data) {
            showMixedColumnChart(data.votes, data.districts)
        },
        error: function(data) {}
    })
}

function getEducation() {
    init();
    $('.col-md-9').css('width','100%');
    $.ajax({
        url: "/RSO/default/getEducationPercentageData.json",
        success: function(data) {
            showMixedColumnChart(data.votes, data.districts)
        },
        error: function(data) {}
    })
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

function getSexList() {
    init();
    $('.col-md-9').css('width','75%');
    showSexBlock();
}

function showColumnChart(data, labels)
{
    var ctx = document.getElementById("columnChart");
    $(ctx).show();
    var columnChart = new Chart(ctx, {
        type: 'bar',
       
        data: {
            labels: labels,
            datasets: [{
                label: '# głosów',
                data: data,
                backgroundColor: getRandomColor()
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
    window.currentChart = columnChart;
}

function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function showMixedColumnChart(data, labels)
{
    var keys = Object.keys(data);
    var ourDataset = [];
    for(i = 0; i < keys.length; i++)
    {
        
        ourDataset.push({
                label:keys[i],
                data:data[keys[i]],
                backgroundColor: getRandomColor()
            });
    }
    
    var ctx = document.getElementById("mixedColumnChart");
    $(ctx).show();
    var columnChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: ourDataset,
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
    window.currentChart = columnChart;
}


function showPieChart(data, labels)
{
    if(data.length != 0 && labels.length != 0)
    {
        var colorList = [];
        labels.forEach(function (){
            colorList.push(getRandomColor());
        });
        
        var ctx = document.getElementById("pieChart");
        $(ctx).show();
        var pieChart = new Chart(ctx,{
            type: 'pie',
            data: data = {
                labels: labels,
                datasets: [
                    {
                        data: data,
                        backgroundColor: colorList,
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
        $(currentChart.chart.canvas).hide();
    }   
}

//****** BLOCK WITH PARTY LIST ******

function showPartyBlock()
{
    
    
    var partyBlock = document.getElementById("partyBlock");
    while (partyBlock.firstChild) {
    partyBlock.removeChild(partyBlock.firstChild);
    }
    
    
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
            
            $('#partyPanel').fadeIn();
            $('#chooseParameterMessage').html("Proszę wybrać partię").show();
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
    btn.setAttribute("class", "btn btn-danger");   
    btn.setAttribute("style", "width: inherit");
    var text = document.createTextNode(element);
    
    btn.appendChild(text);
    btn.addEventListener('click', function(){
        $('#chooseParameterMessage').hide();
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
            
            $('#sexPanel').fadeIn();
            $('#chooseParameterMessage').html("Proszę wybrać płeć").show();
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
    btn.setAttribute("class", "btn btn-danger");   
    btn.setAttribute("style", "width: inherit");
    var text = document.createTextNode(element);
    
    btn.appendChild(text);
    btn.addEventListener('click', function(){
        $('#chooseParameterMessage').hide();
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

function showCandidatesPanel(){
    $('.charts').hide();
    $('#candidates').show();
    $.ajax({
        url: "/RSO/default/candidates.json",
        success: function(data) {
            
            var html = "<ul>";
            
           $.each(Object.keys(data), function(partyIndex, partyName) {
               
               html += "<li>" + partyName + "<ol>";
               
               $.each(data[partyName], function(candidateIndex, candidateName){
                   html += "<li>";
                   html += candidateName;
                   html += "</li>";
               })
               html += "</li></ol>";
           });
            
            html += "</ul>";
            
            $('#candidates').html(html);
        },
        error: function(data) {
            debugger;
        }
    })
}
