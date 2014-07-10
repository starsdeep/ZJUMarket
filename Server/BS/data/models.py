from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import post_save

from django.utils import simplejson
from django.core import serializers

class UserProfile(models.Model):  
	user = models.ForeignKey(User, unique=True)
	Money = models.DecimalField(max_digits=19, decimal_places=2)

	def __unicode__(self):
		return u'Profile of user: %s' % self.user.username


class Interest(models.Model):
	# Keyword = models.CharField(max_length=255)
	Keyword = models.CharField(max_length=255)
	Title = models.CharField(max_length=255)
	Price = models.DecimalField(max_digits=19, decimal_places=2)
	Picture = models.CharField(max_length=255)
	href = models.CharField(max_length=255)
	Category = models.CharField(max_length=10)

class User_inter_Interest(models.Model):
	Id = models.ForeignKey(User)
	Interest = models.CharField(max_length=255)

class Temporary_Query(models.Model):
	Keyword = models.CharField(max_length=255)
	# Keyword = models.ForeignKey(Keyword)
	Title = models.CharField(max_length=255)
	Price = models.DecimalField(max_digits=19, decimal_places=2)
	Picture = models.CharField(max_length=255)
	href = models.CharField(max_length=255)
	Category = models.CharField(max_length=10)

	def __unicode__(self):
		return u'%s' % (self.Title)



class QuerySetEncoder( simplejson.JSONEncoder ):
    def default( self, object ):
        try:
            return serializers.serialize( "python", object,ensure_ascii = False )
        except:
            return simplejson.JSONEncoder.default( self, object )

class Paid_Record(models.Model):
	User = models.ForeignKey(User)
	PID = models.ForeignKey(Temporary_Query)
	isBuy = models.BooleanField()