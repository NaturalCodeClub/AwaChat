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