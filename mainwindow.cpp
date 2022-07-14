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
    this->ui->chat_list->setVerticalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    this->ui->chat_list->setHorizontalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    this->ui->textEdit->setVerticalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    this->ui->textEdit->setHorizontalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    connect(ui->modules_list,&QListWidget::currentRowChanged,ui->stackedWidget,&QStackedWidget::setCurrentIndex);
    connect(ui->friends_list,&QListWidget::currentRowChanged,this,&MainWindow::SelectedFriendTarget);
    connect(ui->but_send,&QPushButton::clicked,this,&MainWindow::OnButSend);
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
            if(result_login)
                this->user=user;
            return result_login;
        }
    }
    return true;
}

void MainWindow::ReadData()
{
    QByteArray buffer;
    buffer=socket->readAll();
   // QMessageBox::information(nullptr,"Message",buffer);
    if(!buffer.isEmpty())
    {
        //ui->textEdit->setText(ui->textEdit->toPlainText()+tr(buffer));
        QJsonDocument doc=QJsonDocument::fromJson(buffer);
        QJsonObject obj=doc.object();
        QString head=obj["head"].toString();
        QJsonArray tags=obj["tags"].toArray();
        //QMessageBox::information(nullptr,"Message",head);
        if(head=="login_response")
        {
            if(tags.isEmpty())
                return;
            QString result=tags[0].toString();
            if(result=="finished")
            {
                result_login = true;
                socket->write(MessagePackage::GetInfoPackage());
            }else result_login= false;
        }else if(head=="userinfo")
        {
            //QMessageBox::information(nullptr,"User Info","User Info");
            QJsonArray array=obj["data"].toArray();
            //QMessageBox::information(nullptr,"Count",QString::number(array.count()));
            auto a=obj["data"].toArray().first();
            QJsonDocument doc_data=QJsonDocument::fromJson(a.toString().toUtf8());

            QJsonObject info=doc_data.object();
            if(!info.isEmpty())
            {
                QJsonArray arr=info["friends"].toArray();
                for(auto i : arr)
                {
                    this->frineds.push_back(i.toString());
                    this->ui->friends_list->addItem(i.toString());
                }
            }
        }else if(head=="schat")
        {
//            QMessageBox::information(nullptr,"Msg",buffer);
            QJsonArray data=obj["data"].toArray();
            if(tags[0].toString()=="private")
            {
                QString target=tags[1].toString();
                history[target].push_back(Message::Create(target,false,data[0].toString()));
                if(now_target==target)
                {
                    ChatMessageBox::Create(QPixmap("://images/icon_user.png"),false,data[0].toString(),this->ui->chat_list);
                    this->ui->chat_list->scrollToBottom();
                }

            }
        }
    }
}

void MainWindow::SelectedFriendTarget(int row)
{
    if(now_target==frineds[row])
        return;
    this->now_target=frineds[row];
    this->ui->label_target->setText(now_target);
    this->ui->chat_list->clear();
    std::vector<Message*> his_target=history[now_target];
    for(Message *i : his_target)
    {
        ChatMessageBox::Create(QPixmap("://images/icon_user.png"),i->type,i->text,this->ui->chat_list);
    }
    this->ui->chat_list->scrollToBottom();
}

void MainWindow::OnButSend()
{
    QString text=this->ui->textEdit->toPlainText();
    ChatMessageBox::Create(QPixmap("://images/icon_user.png"), true,text,this->ui->chat_list);
    this->ui->textEdit->clear();
    Message *msg=Message::Create(now_target,true,text);
    history[now_target].push_back(msg);
    this->ui->chat_list->scrollToBottom();
    QMessageBox::information(nullptr,"Msg",MessagePackage::ChatPackage(msg,now_target));
    socket->write(MessagePackage::ChatPackage(msg,now_target));
}

MainWindow::~MainWindow()
{
    delete ui;
}

