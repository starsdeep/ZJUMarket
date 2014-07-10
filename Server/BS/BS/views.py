#!/usr/bin/env python
# -*- coding: utf-8 -*-

import re
import json
import urllib2
import decimal
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
        if request.GET['key'] != '':
            keyword = request.GET["key"]
            request.session["keyword"] = keyword
            print keyword

            keyword = keyword.encode('utf-8') 

            # may occur the encoding problem
            # keyword = request.POST["key"]
            #keyword = keyword.encode('utf-8')

            taobao = 'http://s.taobao.com/search?q='
            jd = 'http://m.jd.com/ware/search.action?cid=0&keyword='
            amazon = 'http://www.amazon.cn/gp/aw/s/ref=is_pn_1?page='
            # regex
            m=re.compile(r'\d+')

            # key = Keyword(Keyword = keyword)
            # key.save()
            # print key.Keyword

            # request.session["keyword"] = Keyword

            try:
                Temporary_Query.objects.get(Keyword=keyword)
            except Temporary_Query.DoesNotExist:
        
            #add if to judge the keyword exits or not
            #Taobao
                print 'not exist'
                for j in range(0,1):
                    newurl = taobao + keyword + '&style=grid' + "&s=" + str(j*44)
                    # print newurl
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

                        q = Temporary_Query(Keyword = keyword,Title = title[i].text.strip(),\
                            Price = p, Picture = d, href = dd,Category='淘宝')
                        q.save()
                        # if not Interest.objects.filter(Keyword = keyword).exists():
                        if i < 3:
                            if not Interest.objects.filter(href = dd).exists():
                                qq = Interest(Keyword = keyword,Title = title[i].text.strip(),\
                                    Price = p, Picture = d, href = dd,Category='淘宝')
                                qq.save()
                        i=i+1
            #jd
                for j in range(1,4):
                    newurl = jd + keyword + '&sort=0&page='+str(j)
                    #print newurl
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
                for j in range(1,9):
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

                # if 'android' in request.POST:
                Result = simplejson.dumps( objects, cls = QuerySetEncoder )
                return HttpResponse(Result)
                
                # return render_to_response('index.html',{'result':objects},context_instance=RequestContext(request))
                # modify
                # print objects



        elif 'query' in request.POST and request.POST["key"] =='':
            return render_to_response('index.html')
        # order

        elif 'order' in request.GET:
            print 'order'
        # elif 'asc' == request.GET["order"]
            print 'order'
            objects = Temporary_Query.objects.filter(Keyword=request.session["keyword"]).order_by('Price')
            print len(objects)
            Result = simplejson.dumps( objects, cls = QuerySetEncoder )
            return HttpResponse(Result)
            return render_to_response('index.html',{'result':objects},context_instance=RequestContext(request))
        elif 'des' in request.GET:
            objects = Temporary_Query.objects.filter(Keyword=request.session["keyword"]).order_by('-Price')
            Result = simplejson.dumps( objects, cls = QuerySetEncoder )
            return HttpResponse(Result)
            return render_to_response('index.html',{'result':objects},context_instance=RequestContext(request))

        else:
            return HttpResponse('False')

    # not post,refresh
    else:
        print 'getget'
        if 'asc' in request.GET:
            print 'asc'
            objects = Temporary_Query.objects.filter(Keyword=request.session["keyword"]).order_by('Price')
            Result = simplejson.dumps( objects, cls = QuerySetEncoder )
            return HttpResponse(Result)
            return render_to_response('index.html',{'result':objects},context_instance=RequestContext(request))
        elif 'des' in request.GET:
            objects = Temporary_Query.objects.filter(Keyword=request.session["keyword"]).order_by('-Price')
            Result = simplejson.dumps( objects, cls = QuerySetEncoder )
            return HttpResponse(Result)
            return render_to_response('index.html',{'result':objects},context_instance=RequestContext(request))  
        else:
        	print 'error'
        	return HttpResponse('error')
        # return render_to_response('index.html',{'result':objects},context_instance=RequestContext(request))         


def success(request):
    return render_to_response('success.html')
# def register(request):
#     print 'register'

#     if request.method == 'POST':
#         form = UserCreationForm(request.POST)
#         # RegisterForm
#         # form = UserForm(request.POST)
#         # print form
#         if form.is_valid():
#             new_user = form.save()
#             return HttpResponseRedirect("../success")
#     else:
#         form = UserCreationForm()
#         # form = UserForm()
#     return render_to_response('register.html',{'form':form},context_instance=RequestContext(request))



def register(request):
    form = RegisterForm() 
    if request.method == 'GET':   
        return render_to_response("register.html",{'form':form},context_instance=RequestContext(request))
    # if request.method=="POST":
    #     form=RegisterForm(request.POST.copy())
    #     print 'copy form'
    #     if form.is_valid():
    #         username=form.cleaned_data["username"]
    #         email=form.cleaned_data["email"]
    #         password=form.cleaned_data["password"]
    #         user=User.objects.create_user(username,email,password)
    #         user.save()
    #         return HttpResponse('True')
    #     else:
    #         return HttpResponse('False')
    if request.method == 'POST':
        print 'post'
        username = request.POST['username']
        password = request.POST['password']
        print username
        print password
        try:
            user = User.objects.create_user(username,'',password)
            user.save()
            return HttpResponse('True')
        except:
            return HttpResponse('已存在相同用户')

    else:
        return HttpResponse('服务器连接错误')

            # return HttpResponseRedirect("../success")

    return render_to_response("register.html",{'form':form},context_instance=RequestContext(request))

def post(request):
	return render_to_response("post.html",context_instance=RequestContext(request))
	
def login(request):
    if request.method == 'POST':
        print 'post'
    	username = request.POST['username']
        password = request.POST['password']
        user = auth.authenticate(username=username, password=password)

        if user is not None and user.is_active:
            # Correct password, and the user is marked "active"
            auth.login(request, user)
            request.session["username"] = username

            print user

            return HttpResponse('True')
            # return render_to_response("index.html",{"username": request.session["username"]},context_instance=RequestContext(request))
        else:
            # Show an error page
            print 'failure'
            return HttpResponse('False')
            # return render_to_response("login_view.html",{'fail':'fail'},context_instance=RequestContext(request))

    else:
        print 'get'
        return HttpResponse('get')
        # return render_to_response("login_view.html",context_instance=RequestContext(request))

def login_view(request):
    if request.method == 'GET':

        username = request.GET['name']
        password = request.GET['password']
        user = auth.authenticate(username=username, password=password)

        if user is not None and user.is_active:
            # Correct password, and the user is marked "active"
            auth.login(request, user)
            request.session["username"] = username
            # Redirect to a success page.
            #return render_to_response("index.html",user)
            # return render_to_response("index.html", { "info": "success"})
            #return HttpResponseRedirect("/index")
            # if 'android' in request.POST:
            # return HttpResponse(simplejson.dumps( 'True', cls = QuerySetEncoder ))
            print user
            print 'ssss'
            return HttpResponse('True')
            return render_to_response("index.html",{"username": request.session["username"]},context_instance=RequestContext(request))
        else:
            # Show an error page
            print 'failure'
            print 's'
            return HttpResponse(simplejson.dumps( 'False', cls = QuerySetEncoder ))
            return render_to_response("login_view.html",{'fail':'fail'},context_instance=RequestContext(request))
    else:
    	print 's11'
        return render_to_response("login_view.html",context_instance=RequestContext(request))

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
	result1 = {'id': "1", 'name': "1"}
	result = simplejson.dumps( result1, cls = QuerySetEncoder )
	result1 = {'id': "2", 'name': "1"}
	result  = result + simplejson.dumps( result1, cls = QuerySetEncoder )
	return HttpResponse( result)
	

def usertest(request):
    user = User.objects.get(username = request.POST['username'])
    print user.get_profile().Money
    if user.get_profile().Money - decimal.Decimal('%.2f' % float(request.POST['money'])) >= 0:
        user.get_profile().Money =  user.get_profile().Money - decimal.Decimal('%.2f' % float(request.POST['money'])) 

        user.get_profile().save()
        print user.get_profile().Money
        return HttpResponse(user.get_profile().Money)
    else:
        return HttpResponse('False')

def buy(request):
    user = User.objects.filter(username = request.GET['username'])
    record = Paid_Record.objects.filter(User = user)
    # print request.GET['username']
    print record[1].PID_id

    for obj in record:
        

    # for myid in record:
    #     print myid.PID_id
    # product = Temporary_Query.objects.filter(id = record.PID)
    return HttpResponse(simplejson.dumps(record, cls = QuerySetEncoder))
