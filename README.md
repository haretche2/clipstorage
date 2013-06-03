Storages the ClipDatas on several servers with the following features:
 - Every ClipData can be saved on several redundant servers
 - Automatic - stand alone, storage server failure recovery (when several storage servers are configured)
 - writes and reads the ClipDatas through FTP


In order to setup a ClipData storage server, debe crearse un usuario telematica en ese servidor.
Luego, the following commands has to be executed on that server:

sudo apt-get install vsftpd

Reemplazar el archivo /etc/vsftpd.conf por lo siguiente:

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
