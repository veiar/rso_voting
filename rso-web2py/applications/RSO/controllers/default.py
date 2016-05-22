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
    response.flash = T("Hello World")
    return dict(message=T('Welcome to web2py!'))

def getDbConnection():
    userName = myconf.get('db.user')
    userPassword = myconf.get('db.password')
    dbUrl = myconf.get('db.url')
    dbPort = myconf.get('db.port')
    dbName = myconf.get('db.name')
    db = DAL('postgres://' + userName + ':' + userPassword + '@' + dbUrl + ':' + str(dbPort) + '/' + dbName)
    return db

def getPartyPercentageData():
    labels = []
    numbers = []
    db = getDbConnection()
    results = db.executesql("""SELECT d_parties.name, res_party_percent.percentage
                    FROM d_parties JOIN res_party_percent ON d_parties.party_id = res_party_percent.party_id""")
    for row in results:
        name, percentage = row
        labels.append(name)
        numbers.append(percentage)

    return dict(labels=labels, votes=numbers)

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
