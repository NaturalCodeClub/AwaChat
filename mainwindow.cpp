#include "mainwindow.h"
#include "./ui_mainwindow.h"
#include "MessagePackage.h"
#include <QMessageBox>
#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonArray>
#include "ChatMessageBox.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    socket=new QTcpSocket();
    connect(socket,&QTcpSocket::readyRead,this,&MainWindow::ReadData);
    QListWidgetItem *item=new QListWidgetItem();
    ChatMessageBox *msg=new ChatMessageBox();
    msg->setAvatar(QPixmap("://images/icon_user.png"));
    msg->setOndir(false);
    msg->setText("我都不知道怎么办了你说呢, 我怎么知道啊\n给爷爬\n!\n!\n!");
    //msg->repaint();
    msg->updatedata();
    item->setSizeHint(QSize(ui->chat_list->width()-5,msg->getItemHeight()));
    ui->chat_list->addItem(item);
    ui->chat_list->setItemWidget(item,msg);
}

bool MainWindow::login(QString user, QString pwd)
{
    socket->disconnected();
    socket->connectToHost("127.0.0.1",2344);
    if(!socket->waitForConnected(2000))
    {
        QMessageBox::information(nullptr,"提示","无法连接服务器请检查网络设置");
        return false;
    }
    socket->write(MessagePackage::LoginPackage(user,pwd));
    if(socket->waitForBytesWritten(5000))
    {
        if(socket->waitForReadyRead(5000))
        {
            return result_login;
        }
    }
    return true;
}

void MainWindow::ReadData()
{
    QByteArray buffer;
    buffer=socket->readAll();
    if(!buffer.isEmpty())
    {
        //ui->textEdit->setText(ui->textEdit->toPlainText()+tr(buffer));
        QJsonDocument doc=QJsonDocument::fromJson(buffer);
        QJsonObject obj=doc.object();
        QString head=obj["head"].toString();
        QJsonArray tags=obj["tags"].toArray();
        if(tags.isEmpty())
            return;
        if(head=="login_response")
        {
            QString result=tags[0].toString();
            if(result=="finished")
            {
                result_login = true;
            }else result_login= false;
        }
    }
}

MainWindow::~MainWindow()
{
    delete ui;
}

