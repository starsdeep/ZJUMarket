from django.contrib import admin
from data.models import *

# Register your models here.
admin.site.register(Temporary_Query)
admin.site.register(Paid_Record)

class UserProfileAdmin(admin.ModelAdmin):  
    fields = ('user','Money',)  
  
admin.site.register(UserProfile, UserProfileAdmin) 