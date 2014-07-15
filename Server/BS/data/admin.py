from django.contrib import admin
from data.models import *

# Register your models here.
admin.site.register(Temporary_Query)
admin.site.register(Paid_Record)
# admin.site.register(Paid_Record)

class UserProfileAdmin(admin.ModelAdmin):  
    fields = ('user','Money','Address')  
  
admin.site.register(UserProfile, UserProfileAdmin) 