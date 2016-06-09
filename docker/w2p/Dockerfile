FROM mateuszj92/w2p_2_py

EXPOSE 443:443

WORKDIR /root/myapp
CMD python2.7 web2py.py -k myapp.key -c myapp.crt -i 0.0.0.0 -p 443 -a 'admin'
