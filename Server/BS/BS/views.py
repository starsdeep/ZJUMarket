#!/usr/bin/env python
# -*- coding: utf-8 -*-

import re
# import json
import urllib2
import decimal
import chardet
import datetime
from django import forms
from data.models import *
from django.template import *
from bs4 import BeautifulSoup
from forms import RegisterForm
from django.contrib import auth
from django.http import HttpResponse
from django.core.paginator import Paginator
from django.http import HttpResponseRedirect
from django.forms.models import model_to_dict
from django.core.exceptions import ObjectDoesNotExist
from django.contrib.auth.forms import UserCreationForm
from django.shortcuts import render_to_response,RequestContext

def hello(request):
    return HttpResponse("Hello world")

def helloParam(request, param1):
    return HttpResponse("The param is : " + param1)

def test(request):
    return render_to_response("test.html",context_instance=RequestContext(request))
    #context_instance is to assign the csrf value to the test.html

def index(request):
    if request.user.is_authenticated():
        print 'login success'
    else:
        print 'login failure'

    return render_to_response("index.html",context_instance=RequestContext(request))

def search(request):
    print 'search'
    if request.method == "GET":
        # if 'query' in request.POST and request.POST["key"] !='':
        if 'key' in request.GET and request.GET['key'] != '':
            keyword = request.GET["key"]
            request.session["keyword"] = keyword
            # if isinstance(keyword,'gbk'):
            #     print '11111'
            print keyword
            # print chardet.detect(keyword)

            # keyword = keyword.encode('utf-8') 

            # may occur the encoding problem
            # keyword = request.POST["key"]
            #keyword = keyword.encode('utf-8')

            taobao = 'http://s.taobao.com/search?q='
            jd = 'http://m.jd.com/ware/search.action?cid=0&keyword='
            amazon = 'http://www.amazon.cn/gp/aw/s/ref=is_pn_1?page='
            # regex
            m=re.compile(r'\d+')

            try:
                Temporary_Query.objects.get(Keyword=keyword)
            except Temporary_Query.DoesNotExist:
        
            #add if to judge the keyword exits or not
            #Taobao
                # mykey = keyword.encode('gbk')
                # print 'not exist'
                # for j in range(0,1):
                #     newurl = taobao + mykey + '&style=grid' + "&s=" + str(j*44)
                #     # print newurl
                #     doc = urllib2.urlopen(newurl)
                #     tmp = doc.read()
                #     soup = BeautifulSoup(tmp, "html.parser")
                    
                #     price = soup.find_all('div',class_='col price')
                #     title = soup.find_all('h3','summary')
                #     image = soup.find_all('p','pic-box')

                #     i = 0
                #     for tmp in price:
                #         if i<10:
                #             a,b,c = str(image[i].img).partition('data-ks-lazyload="')
                #             d,e,f = c.partition('" resy-img')

                #             aa,bb,cc = str(image[i].a).partition('href="')
                #             dd,ee,ff = cc.partition('" target="')

                #             mm = m.findall(price[i].text)
                #             p = float(''.join(mm[0:len(mm)-1]))

                #             q = Temporary_Query(Keyword = keyword,Title = title[i].text.strip(),\
                #                 Price = p, Picture = d, href = dd,Category='淘宝')
                #             q.save()
                #         # if not Interest.objects.filter(Keyword = keyword).exists():
                #         if i < 3:
                #             if not Interest.objects.filter(href = dd).exists():
                #                 qq = Interest(Keyword = keyword,Title = title[i].text.strip(),\
                #                     Price = p, Picture = d, href = dd,Category='淘宝')
                #                 qq.save()
                #         i=i+1
            #jd
                mykey = keyword.encode('utf-8')
                for j in range(1,2):
                    newurl = jd + mykey + '&sort=0&page='+str(j)
                    #print newurl
                    doc = urllib2.urlopen(newurl)
                    tmp = doc.read()
                    soup = BeautifulSoup(tmp, "html.parser")

                    price = soup.find_all('div','price')
                    title = soup.find_all('div','title')
                    image = soup.find_all('div','pic')

                    i = 0
                    for tmp in price:
                        if i<10:
                            a,b,c = str(image[i].img).partition('src="')
                            d,e,f = c.partition('"')

                            aa,bb,cc = str(title[i].a).partition('/product/')
                            dd,ee,ff = cc.partition('"')

                            mm = m.findall(price[i].text.encode('utf-8').lstrip('京东价：'))
                            p = float(''.join(mm[0:len(mm)-1]))

                            q = Temporary_Query(Keyword = keyword,Title = title[i].text.strip(),\
                                Price = p, Picture = d, href = "http://item.jd.com/"+dd,Category='京东')
                            q.save()
                        # if not Interest.objects.filter(Keyword = keyword).exists():
                        if i < 3 and j == 1:
                            if not Interest.objects.filter(href = "http://item.jd.com/"+dd).exists():
                                qq = Interest(Keyword = keyword,Title = title[i].text.strip(),\
                                Price = p, Picture = d, href = "http://item.jd.com/"+dd,Category='京东')
                                qq.save()
                        i=i+1
            #amazon
                mykey = keyword.encode('utf-8')
                for j in range(1,3):
                    url = 'http://www.amazon.cn/gp/aw/s/ref=is_pn_1?page=' + str(j) + '&keywords='+ mykey
                    doc = urllib2.urlopen(url)
                    tmp = doc.read()
                    soup = BeautifulSoup(tmp, "html.parser")

                    price = soup.find_all('span','dpOurPrice')
                    title = soup.find_all('span','productTitle')
                    image = soup.find_all('div','toTheEdge productList')

                    i = 0
                    for tmp in price:
                        a,b,c = str(image[i].img).partition('src="')
                        d,e,f = c.partition('"')

                        aa,bb,cc = str(title[i].a).partition('/gp/aw/d/')
                        dd,ee,ff = cc.partition('/ref=')

                        a,b,t = title[i].text.strip().partition('.')


                        mm = m.findall(price[i].text)
                        p = float(''.join(mm[0:len(mm)-1]))

                        q = Temporary_Query(Keyword = keyword,Title = t,\
                            Price = p, Picture = d, href = 'http://www.amazon.cn/dp/'+dd,Category='亚马逊')
                        q.save()
                        # if not Interest.objects.filter(Keyword = keyword).exists():
                        if i < 3 and j == 1:
                            if not Interest.objects.filter(href = 'http://www.amazon.cn/dp/'+dd).exists():
                                qq = Interest(Keyword = keyword,Title = t,\
                                Price = p, Picture = d, href = 'http://www.amazon.cn/dp/'+dd,Category='亚马逊')
                                qq.save()
                        i=i+1

            except Temporary_Query.MultipleObjectsReturned:
                print 'multi'
            finally:
                objects = Temporary_Query.objects.filter(Keyword=request.session["keyword"])
                print len(objects)

                if 'type' in request.GET and request.GET['type'] == 'android':
                    Result = simplejson.dumps( objects, cls = QuerySetEncoder )
                    return HttpResponse(Result)
                else:
                    return render_to_response('index.html',{'result':objects},context_instance=RequestContext(request))
                # modify
                # print objects
        else:
            if 'asc' in request.GET:
                print 'asc'
                objects = Temporary_Query.objects.filter(Keyword=request.session["keyword"]).order_by('Price')
                return render_to_response('index.html',{'result':objects},context_instance=RequestContext(request))
            elif 'des' in request.GET:
                objects = Temporary_Query.objects.filter(Keyword=request.session["keyword"]).order_by('-Price')
                return render_to_response('index.html',{'result':objects},context_instance=RequestContext(request))  
            else:
                if 'type' in request.GET and request.GET['type'] == 'android':
                    objects = Temporary_Query.objects.filter(Keyword=request.session["keyword"])
                    Result = simplejson.dumps( objects, cls = QuerySetEncoder )
                    return HttpResponse(Result)
                else:
                    objects = Temporary_Query.objects.filter(Keyword=request.session["keyword"])
                    return render_to_response('index.html',{'result':objects},context_instance=RequestContext(request))


def success(request):
    return render_to_response('success.html')

def register(request):
    form = RegisterForm() 
    if request.method == 'GET':   
        return render_to_response("register.html",{'form':form},context_instance=RequestContext(request))

    if request.method == 'POST':
        print 'post'
        username = request.POST['username']
        password = request.POST['password']
        print username
        print password
        try:
            user = User.objects.create_user(username,'',password)
            user.save()
            try:

                user.get_profile()
            except:
                profile = UserProfile(user = user)
                profile.save()

            if 'type' in request.POST and request.POST['type'] == 'android':
                return HttpResponse('True')
            else:
                return HttpResponseRedirect("../success")
        except:
            return HttpResponse('已存在相同用户')

    else:
        return HttpResponse('服务器连接错误')

            # return HttpResponseRedirect("../success")

    return render_to_response("register.html",{'form':form},context_instance=RequestContext(request))

def post(request):
	return render_to_response("post.html",context_instance=RequestContext(request))
	
def login(request):
        return render_to_response("login_view.html",context_instance=RequestContext(request))

def login_view(request):
    if request.method == 'POST':
        print 'post'
        username = request.POST['username']
        password = request.POST['password']
        user = auth.authenticate(username=username, password=password)
        print username
        print password
        # print request.POST['type']


        if user is not None and user.is_active:
            # Correct password, and the user is marked "active"
            auth.login(request, user)
            request.session["username"] = username
            user = User.objects.get(username = username)
            try:

                user.get_profile()
            except:
                profile = UserProfile(user = user)
                profile.save()
            money = user.get_profile().Money
            address = user.get_profile().Address
            order = Paid_Record.objects.filter(User = user).count()
            paid = Paid_Record.objects.filter(User = user, isBuy = True).count()
            email = user.email


            print user
            if 'type' in request.POST and request.POST['type'] == 'android':
                # return HttpResponse('True')
                dict = {}
                week = datetime.timedelta(days=-7)
                week_in_money = Money.objects.filter(Date__gte=datetime.datetime.now().date()+week, inORout = True)
                week_out_money = Money.objects.filter(Date__gte=datetime.datetime.now().date()+week, inORout = False)

                weekdict = {}
                inmoney = 0
                outmoney = 0
                for week in week_in_money:
                    inmoney = inmoney + week.Money
                for week in week_out_money:
                    outmoney = outmoney + week.Money    

                dict['islogin'] = 'true'
                # islogin:str (ture/false 也是str)
                dict['falsehint'] = '用户名或密码错误'
                dict['username'] = username
                dict['password'] = password
                dict['balance'] = str(money)
                dict['recordCount'] = str(order)
                dict['historyCount'] = str(paid)
                dict['email'] = email
                dict['address'] = address
                dict['week_in'] = str(inmoney)
                dict['week_out'] = str(outmoney)                

                res = simplejson.dumps(dict,cls = QuerySetEncoder)
                return HttpResponse(res)

            else:
                return render_to_response("index.html",{"username": request.session["username"]},context_instance=RequestContext(request))
        else:
            # Show an error page
            print 'failure'
            if 'type' in request.POST and request.POST['type'] == 'android':
                return HttpResponse('False')
            else:
                return render_to_response("login_view.html",{'fail':'fail'},context_instance=RequestContext(request))

    else:
        print 'get'
        return HttpResponse('get method')

def logout_view(request):
    auth.logout(request)
    # Redirect to a success page.
    return HttpResponseRedirect("/index")

def profile(request):
    username = request.session["username"]
    user = User.objects.get(username = username)
    dict={}
    objects = User_inter_Interest.objects.filter(Id = user)
    for interest in objects:
        print interest.Interest
        queryset = Interest.objects.filter(Keyword = interest.Interest)
        dict[interest.Interest] = queryset

    if request.method == 'POST':
        keyword = request.POST['keyword']
        if keyword != '':
            if not User_inter_Interest.objects.filter(Id = user,Interest = keyword):
                p = User_inter_Interest(Id = user,Interest = keyword)
                p.save()


            if not Interest.objects.filter(Keyword = keyword).exists():    
                taobao = 'http://s.taobao.com/search?q='
                jd = 'http://m.jd.com/ware/search.action?cid=0&keyword='
                amazon = 'http://www.amazon.cn/gp/aw/s/ref=is_pn_1?page=' 
                m=re.compile(r'\d+') 
            #taobao
                for j in range(0,1):
                    newurl = taobao + keyword + '&style=grid' + "&s=0"
                    doc = urllib2.urlopen(newurl)
                    tmp = doc.read()
                    soup = BeautifulSoup(tmp, "html.parser")
                    
                    price = soup.find_all('div',class_='col price')
                    title = soup.find_all('h3','summary')
                    image = soup.find_all('p','pic-box')

                    i = 0
                    for tmp in price:
                        a,b,c = str(image[i].img).partition('data-ks-lazyload="')
                        d,e,f = c.partition('" resy-img')

                        aa,bb,cc = str(image[i].a).partition('href="')
                        dd,ee,ff = cc.partition('" target="')

                        mm = m.findall(price[i].text)
                        p = float(''.join(mm[0:len(mm)-1]))
                        if i < 3:
                            if not Interest.objects.filter(href = dd).exists():
                                qq = Interest(Keyword = keyword,Title = title[i].text.strip(),\
                                    Price = p, Picture = d, href = dd,Category='淘宝')
                                qq.save()
                        i=i+1  
            #jd
                for j in range(1,2):
                    newurl = jd + keyword.encode('utf-8') + '&sort=0&page='+str(j)
                    doc = urllib2.urlopen(newurl)
                    tmp = doc.read()
                    soup = BeautifulSoup(tmp, "html.parser")

                    price = soup.find_all('div','price')
                    title = soup.find_all('div','title')
                    image = soup.find_all('div','pic')

                    i = 0
                    for tmp in price:
                        a,b,c = str(image[i].img).partition('src="')
                        d,e,f = c.partition('"')

                        aa,bb,cc = str(title[i].a).partition('/product/')
                        dd,ee,ff = cc.partition('"')

                        mm = m.findall(price[i].text.encode('utf-8').lstrip('京东价：'))
                        p = float(''.join(mm[0:len(mm)-1]))
                        if i < 3 and j == 1:
                            if not Interest.objects.filter(href = "http://item.jd.com/"+dd).exists():
                                qq = Interest(Keyword = keyword,Title = title[i].text.strip(),\
                                Price = p, Picture = d, href = "http://item.jd.com/"+dd,Category='京东')
                                qq.save()
                        i=i+1
            #amazon
                for j in range(1,2):
                    url = 'http://www.amazon.cn/gp/aw/s/ref=is_pn_1?page=' + str(j) + '&keywords='+ keyword
                    doc = urllib2.urlopen(url)
                    tmp = doc.read()
                    soup = BeautifulSoup(tmp, "html.parser")

                    price = soup.find_all('span','dpOurPrice')
                    title = soup.find_all('span','productTitle')
                    image = soup.find_all('div','toTheEdge productList')

                    i = 0
                    for tmp in price:
                        a,b,c = str(image[i].img).partition('src="')
                        d,e,f = c.partition('"')

                        aa,bb,cc = str(title[i].a).partition('/gp/aw/d/')
                        dd,ee,ff = cc.partition('/ref=')
                        a,b,t = title[i].text.strip().partition('.')

                        mm = m.findall(price[i].text)
                        p = float(''.join(mm[0:len(mm)-1]))
                        if i < 3 and j == 1:
                            if not Interest.objects.filter(href = 'http://www.amazon.cn/dp/'+dd).exists():
                                qq = Interest(Keyword = keyword,Title = t,\
                                Price = p, Picture = d, href = 'http://www.amazon.cn/dp/'+dd,Category='亚马逊')
                                qq.save()
                        i=i+1  
    dict={}
    objects = User_inter_Interest.objects.filter(Id = user)
    for interest in objects:
        queryset = Interest.objects.filter(Keyword = interest.Interest)
        dict[interest.Interest] = queryset      
    return render_to_response('profile.html',{'result':objects,'Queryset':dict},context_instance=RequestContext(request)) 

def jtest(request):
	print '111'
    # print request.GET['username']
	result1 = {'id': "1", 'name': "1"}
	result = simplejson.dumps( result1, cls = QuerySetEncoder )
	result1 = {'id': "2", 'name': "1"}
	result  = result + simplejson.dumps( result1, cls = QuerySetEncoder )
	return HttpResponse( result)
	

def usertest(request):
    print 'pay'
    print request.method
    print request.POST['username']
    user = User.objects.get(username = request.POST['username'])
    dict = {}

    try:
        if request.POST['type'] == 'NFC':
            print 'afsdfd'
            print request.POST['ID']
            record = Paid_Record.objects.get(User = user, CID = request.POST['ID'] , isBuy = False)
        else:
            product = Temporary_Query.objects.get(id = request.POST['ID'])
            # record = Paid_Record.objects.get(User = user, PID = request.POST['ID'] , isBuy = False)
            record = Paid_Record.objects.get(User = user, PID  = product,isBuy = False)

        cost = record.PID.Price
        if user.get_profile().Money - cost >=0:
            user.get_profile().Money = user.get_profile().Money - cost
        #     user.get_profile().Money =  user.get_profile().Money - decimal.Decimal('%.2f' % float(request.POST['money'])) 

            user.get_profile().save()

            q = Money(user = user, Balance = user.get_profile().Money, Money = cost, inORout = False)
            q.save()

            record.isBuy = True
            record.save()
        #     print user.get_profile().Money
            dict['money'] = str(user.get_profile().Money)
            dict['state'] = 'True'
            res = simplejson.dumps(dict,cls = QuerySetEncoder)
            return HttpResponse(res)
        else:
            dict['state'] = 'False'
            res = simplejson.dumps(dict,cls = QuerySetEncoder)
            return HttpResponse(res)
    except:
        dict['state'] = 'Invalid'
        res = simplejson.dumps(dict,cls = QuerySetEncoder)
        return HttpResponse(res)       

    
    # if user.get_profile().Money - decimal.Decimal('%.2f' % float(request.POST['money'])) >= 0:

    # return HttpResponse(cost)

def buy(request):
    user = User.objects.filter(username = request.GET['username'])
    record = Paid_Record.objects.filter(User = user)
    Result = simplejson.dumps( record, cls = QuerySetEncoder )

    for obj in record:
        product = Temporary_Query.objects.filter(id = obj.PID_id)

        Result = Result + simplejson.dumps( product, cls = QuerySetEncoder )

    # Result = simplejson.dumps(Result, cls = QuerySetEncoder)

    return HttpResponse(Result)

def reserve(request):
    username = request.GET['username']
    pid = request.GET['pid']
    user = User.objects.get(username = username)
    product = Temporary_Query.objects.get(id = pid)
    if not Paid_Record.objects.filter(User = user,PID = product):
        q = Paid_Record(User = user,PID = product,isBuy = '')
        q.save()
        return HttpResponse('True')
    else:
        return HttpResponse('False')
def balance(request):
    username = request.POST['username']
    # length = request.POST['length']
    user = User.objects.get(username = username)
    # days = datetime.timedelta(days=-int(length))
    week = datetime.timedelta(days=-7)
    # money = Money.objects.filter('''Date__gte=datetime.datetime.now().date()+days''')
    money = Money.objects.all()[0:100]
    # week_in_money = Money.objects.filter(Date__gte=datetime.datetime.now().date()+week, inORout = True)
    # week_out_money = Money.objects.filter(Date__gte=datetime.datetime.now().date()+week, inORout = False)
    Balancelist = []
    for q in money:
        Balancelist.append(q.Balance)


    # weekdict = {}
    # inmoney = 0
    # outmoney = 0
    # for week in week_in_money:
    #     inmoney = inmoney + week.Money
    # for week in week_out_money:
    #     outmoney = outmoney + week.Money

    # dict['week_in'] = str(inmoney)
    # dict['week_out'] = str(outmoney)
    
    res = simplejson.dumps(Balancelist,cls = QuerySetEncoder)

    return HttpResponse(res)
    # return dict

def add(request):
    username = request.POST['username']
    user = User.objects.get(username = username)
    print datetime.datetime.now().date()
    # return HttpResponse('True')

    for i in range(100,-1,-1):
        print datetime.datetime.now().date() - datetime.timedelta(days = i)
        q = Money(user = user, Balance = user.get_profile().Money, Money = 100, inORout = True , Date = datetime.datetime.now().date() - datetime.timedelta(days = i) )
        q.save()
        user.get_profile().Money = q.Balance + 100
        user.get_profile().save()
    return HttpResponse('True')