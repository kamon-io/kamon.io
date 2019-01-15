FROM nginx:stable-alpine
ADD _site /usr/share/nginx/html
ADD nginx.conf /etc/nginx/nginx.conf
EXPOSE 80