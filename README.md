Almacena ClipDatas en varios servidores con las siguientes caracteristicas:
 - Cada ClipData se puede almacenar en varios servidores en forma redundante
 - Recuperacion automatica de caida de servidor de almacenamiento (cuando varios servidores se encuentran configurados)
 - Guarda y lee los datos por medio de FTP

Para habilitar el archivo de configuración config.properties o el de servidores StorageServers.xml, debe quitarse el "_template" de los ejemplos

Para configurar un servidor de almacenamiento en Ubuntu, debe ejecutar el siguiente comando:

sudo apt-get install vsftpd

Luego reemplazar el archivo /etc/vsftpd.conf por lo siguiente:

 # Put in /etc/vsftpd.conf
 # Don't forget to change samurai into your local username
 listen=YES
 anonymous_enable=YES
 local_enable=YES
 write_enable=YES
 anon_upload_enable=YES
 anon_mkdir_write_enable=YES
 dirmessage_enable=YES
 xferlog_enable=YES
 connect_from_port_20=YES
 chown_uploads=YES
 chown_username=root
 ftpd_banner=Welcome to blah FTP service.
 secure_chroot_dir=/var/run/vsftpd
 pam_service_name=vsftpd
 rsa_cert_file=/etc/ssl/certs/vsftpd.pem
 anon_root=/home/telematica/ftp
 local_root=/home/telematica/ftp

Y ejecutar los siguientes comandos:

sudo chown -cR root /etc/vsftpd.conf
sudo chmod 777 /home/telematica/ftp
sudo /etc/init.d/vsftpd restart
