from django.conf.urls import patterns, include, url
from BS.views import *
from django.contrib import admin
from django.contrib.staticfiles.urls import staticfiles_urlpatterns
from django.contrib.auth.views import  logout
import settings
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'BS.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    (r'^site_media/(?P<path>.*)$','django.views.static.serve',{'document_root': settings.STATIC_PATH}),

    (r'^plist/(.+)/$', helloParam),

    (r'^index/$', index),
    (r'^profile/$', profile),
    # (r'^registry/$', registry),
    (r'^register/$', register),
    (r'^success/$', success),
    (r'^login/$',  login),
    (r'^login_view/$', login_view),
    (r'^logout/$', logout_view),
    (r'^search/$', search),
    (r'^jtest/$', jtest),
    (r'^post/$', post),
    (r'^usertest/$', usertest),
    (r'^buy/$', buy),
    (r'^reserve/$', reserve),
    (r'^balance/$',balance),
    (r'^add/$',add),


    
    

    (r'^test/$', test),
)
