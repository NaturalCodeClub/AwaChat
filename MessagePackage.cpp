#include "MessagePackage.h"
#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonArray>

MessagePackage::MessagePackage(QObject *parent) : QObject(parent)
{

}

MessagePackage::~MessagePackage()
{

}

QByteArray MessagePackage::LoginPackage(QString user, QString pwd)
{
    QJsonObject obj;
    obj.insert("version",-1);
    obj.insert("head","login");
    QJsonArray data;
    data.push_back(user);
    data.push_back(pwd);
    obj.insert("data",data);
    QJsonDocument doc;
    doc.setObject(obj);
    QByteArray str=doc.toJson();
    return str;
}

QByteArray MessagePackage::GetInfoPackage()
{
    QJsonObject object;
    object.insert("version",-1);
    object.insert("head","getuserinfo");
    QJsonDocument doc;
    doc.setObject(object);
    return doc.toJson();
}

QByteArray MessagePackage::ChatPackage(Message *msg, QString msender)
{
    QJsonObject object;
    object.insert("version",-1);
    object.insert("head","chat");
    QJsonArray tags,data;
    tags.push_back("private");
    tags.push_back(msender);
    data.push_back(msg->text);
    object.insert("tags",tags);
    object.insert("data",data);
    QJsonDocument doc;
    doc.setObject(object);
    return doc.toJson();
}