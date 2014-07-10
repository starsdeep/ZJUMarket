#coding=utf-8
from django import forms
from django.contrib.auth.models import User
from django.utils.translation import ugettext_lazy as _

class RegisterForm(forms.Form):
    username=forms.CharField(label=_(u"用户名"),max_length=30,widget=forms.TextInput(attrs={'size': 20,}))
    email=forms.EmailField(label=_(u"邮件"),max_length=30,widget=forms.TextInput(attrs={'size': 30,}))    
    password=forms.CharField(label=_(u"密码"),min_length=6,max_length=30,widget=forms.PasswordInput(attrs={'size': 20,}))
    
    
    def clean_username(self):
        '''验证重复昵称'''
        users = User.objects.filter(username__iexact=self.cleaned_data["username"])
        if not users:
            return self.cleaned_data["username"]
        raise forms.ValidationError(_(u"A user with that username already exists."))
        
    def clean_email(self):
        '''验证重复email'''
        emails = User.objects.filter(email__iexact=self.cleaned_data["email"])
        if not emails:
            return self.cleaned_data["email"]
        raise forms.ValidationError(_(u"A user with that email already exists."))
        
class LoginForm(forms.Form):
    username=forms.CharField(label=_(u"用户名"),max_length=30,widget=forms.TextInput(attrs={'size': 20,}))
    password=forms.CharField(label=_(u"密码"),max_length=30,widget=forms.PasswordInput(attrs={'size': 20,}))
    