# -*- coding: utf-8 -*-
# this file is released under public domain and you can use without limitations

# -------------------------------------------------------------------------
# This is a sample controller
# - index is the default action of any application
# - user is required for authentication and authorization
# - download is for downloading files uploaded in the db (does streaming)
# -------------------------------------------------------------------------
from gluon.debug import dbg

def index():
    """
    example action using the internationalization operator T and flash
    rendered by views/default/index.html or views/generic.html

    if you need a simple wiki simply replace the two lines below with:
    return auth.wiki()
    """
    
    return dict(message=T('RSO'))

def getFrequency():
    
    db = getDbConnection()
    result = db.executesql("""SELECT SUM(votes_sum) FROM res_party_candidates""")
    total = 500.0
    res = 100*result[0][0]/total
    
    return res

def candidates():
    dictionary = None
    
    db = getDbConnection()
    results = db.executesql("""SELECT d_candidates.name, d_candidates.surname, d_parties.name
                                FROM d_candidates JOIN d_parties ON
                                d_candidates.party_id = d_parties.party_id
                                ORDER BY d_parties.name ASC, d_candidates.surname ASC""")
    for row in results:
        name, lastname, partyname = row
        
        if dictionary is None:
            dictionary = {partyname:[name + ", " + lastname]}
            continue

        if partyname in dictionary:
            dictionary[partyname].append(name + ", " + lastname)
        else:
            dictionary[partyname] = [name + ", " + lastname]

    return dictionary

def getDbConnection():
    userName = myconf.get('db.user')
    userPassword = myconf.get('db.password')
    dbUrl = myconf.get('db.url')
    dbPort = myconf.get('db.port')
    dbName = myconf.get('db.name')
    db = DAL('postgres://' + userName + ':' + userPassword + '@' + dbUrl + ':' + str(dbPort) + '/' + dbName)
    return db

def getPartyList():
    labels = []
    numbers = []
    db = getDbConnection()
    results = db.executesql("""SELECT * FROM d_parties """)
    for row in results:
        partyid, name = row
        labels.append(name)
        numbers.append(partyid)

    return dict(names=labels, ids=numbers)

def getCandidatePercentageData():
    partyId = request.vars["partyId"]
    labels = []
    numbers = []
    numbersInPercents = []
    total_votes_sum = 0
    db = getDbConnection()
    results = db.executesql("""SELECT d_candidates.name, d_candidates.surname, res_party_candidates.votes_sum
                                FROM d_candidates JOIN res_party_candidates
                                ON d_candidates.candidate_id = res_party_candidates.candidate_id
                                WHERE d_candidates.party_id = """ + str(partyId))
    for row in results:
        name, lastname, votes_sum = row
        labels.append(lastname + ", " + name)
        numbers.append(votes_sum)
        total_votes_sum += votes_sum

    for number in numbers:
        numbersInPercents.append(number*100/total_votes_sum)

    return dict(labels=labels, votes=numbersInPercents)

def getSexList():
    labels = []
    numbers = []
    db = getDbConnection()
    results = db.executesql("""SELECT * FROM d_sex """)
    for row in results:
        sexid, name = row
        labels.append(name)
        numbers.append(sexid)

    return dict(names=labels, ids=numbers)

def getSexPercentageData():
    sexId = request.vars["sexId"]
    labels = []
    numbers = []
    numbersInPercents = []
    total_votes_sum = 0
    db = getDbConnection()
    results = db.executesql("""SELECT d_parties.name, res_party_sex.votes_sum
                            FROM d_parties JOIN res_party_sex ON
                            d_parties.party_id = res_party_sex.party_id
                            JOIN d_sex ON d_sex.sex_id = res_party_sex.sex_id
                            WHERE d_sex.sex_id = """ + str(sexId))
    for row in results:
        partyname, votes_sum = row
        labels.append(partyname)
        numbers.append(votes_sum)
        total_votes_sum += votes_sum

    for number in numbers:
        numbersInPercents.append(number*100/total_votes_sum)

    return dict(labels=labels, votes=numbersInPercents)

def getPartyPercentageData():
    labels = []
    numbers = []
    numbersInPercents = []
    total_votes_sum = 0
    db = getDbConnection()
    results = db.executesql("""SELECT d_parties.name, res_party_percent.votes_sum
                    FROM d_parties JOIN res_party_percent ON d_parties.party_id = res_party_percent.party_id""")
    for row in results:
        name, votes_sum = row
        labels.append(name)
        numbers.append(votes_sum)
        total_votes_sum += votes_sum

    for num in numbers:
        numbersInPercents.append(int(round(float(num)*100/float(total_votes_sum))))

    return dict(labels=labels, votes=numbersInPercents)


def getConstituenciesPercentageData():
    dictionary = None
    districts = []
    db = getDbConnection()
    results = db.executesql("""SELECT d_parties.name, res_party_constituency.votes_sum,d_constituencies.name
                            FROM d_parties JOIN res_party_constituency ON
                            d_parties.party_id = res_party_constituency.party_id
                            JOIN d_constituencies ON
                            d_constituencies.constituency_id = res_party_constituency.constituency_id
                            ORDER BY d_parties.party_id ASC, d_constituencies.constituency_id ASC""")
    for row in results:
        name, votes_sum, districtName = row

        if districtName not in districts:
            districts.append(districtName)

        if dictionary is None:
            dictionary = {name:[votes_sum]}
            continue

        if name in dictionary:
            dictionary[name].append(votes_sum)
        else:
            dictionary[name] = [votes_sum]

    return dict(votes=dictionary, districts=districts)

def getAgePercentageData():
    dictionary = None
    districts = []
    db = getDbConnection()
    results = db.executesql("""SELECT d_parties.name, res_party_age.votes_sum,d_age.age_group
                            FROM d_parties JOIN res_party_age ON
                            d_parties.party_id = res_party_age.party_id
                            JOIN d_age ON
                            d_age.age_id = res_party_age.age_id
                            ORDER BY d_parties.party_id ASC, d_age.age_id ASC""")
    for row in results:
        name, votes_sum, districtName = row

        if districtName not in districts:
            districts.append(districtName)

        if dictionary is None:
            dictionary = {name:[votes_sum]}
            continue

        if name in dictionary:
            dictionary[name].append(votes_sum)
        else:
            dictionary[name] = [votes_sum]

    return dict(votes=dictionary, districts=districts)

def getEducationPercentageData():
    dictionary = None
    districts = []
    db = getDbConnection()
    results = db.executesql("""SELECT d_parties.name, res_party_education.votes_sum,d_education.education_type
                            FROM d_parties JOIN res_party_education ON
                            d_parties.party_id = res_party_education.party_id
                            JOIN d_education ON
                            d_education.education_id = res_party_education.education_id
                            ORDER BY d_parties.party_id ASC, d_education.education_id ASC""")
    for row in results:
        name, votes_sum, districtName = row

        if districtName not in districts:
            districts.append(districtName)

        if dictionary is None:
            dictionary = {name:[votes_sum]}
            continue

        if name in dictionary:
            dictionary[name].append(votes_sum)
        else:
            dictionary[name] = [votes_sum]

    return dict(votes=dictionary, districts=districts)


	
def user():
    """
    exposes:
    http://..../[app]/default/user/login
    http://..../[app]/default/user/logout
    http://..../[app]/default/user/register
    http://..../[app]/default/user/profile
    http://..../[app]/default/user/retrieve_password
    http://..../[app]/default/user/change_password
    http://..../[app]/default/user/bulk_register
    use @auth.requires_login()
        @auth.requires_membership('group name')
        @auth.requires_permission('read','table name',record_id)
    to decorate functions that need access control
    also notice there is http://..../[app]/appadmin/manage/auth to allow administrator to manage users
    """
    return dict(form=auth())


@cache.action()
def download():
    """
    allows downloading of uploaded files
    http://..../[app]/default/download/[filename]
    """
    return response.download(request, db)


def call():
    """
    exposes services. for example:
    http://..../[app]/default/call/jsonrpc
    decorate with @services.jsonrpc the functions to expose
    supports xml, json, xmlrpc, jsonrpc, amfrpc, rss, csv
    """
    return service()
