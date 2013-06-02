Storages the ClipDatas on several servers with the following features:
 - Every ClipData can be saved on several redundant servers
 - Automatic - stand alone, storage server failure recovery (when several storage servers are configured)
 - writes and reads the ClipDatas through FTP


In order to setup a ClipData storage server, the following commands has to be executed on that server:

sudo apt-get install vsftpd
sed 's/anonymous_enable=NO/anonymous_enable=YES/' /etc/vsftpd.conf > /tmp/vsftpd.conf
sed 's/#write_enable=YES/write_enable=YES/' /tmp/vsftpd.conf > /tmp/vsftpd2.conf
sudo mv /tmp/vsftpd2.conf /etc/vsftpd.conf
sudo /etc/init.d/vsftpd restart

sudo apt-get remove --purge vsftpd
sudo service vsftpd start





sudo apt-get install vsftpd
sudo mkdir /srv/ftp
sudo usermod -d /srv/ftp ftp
sudo /etc/init.d/vsftpd restart

      local_enable=YES
      write_enable=YES

anon_upload_enable=YES
