//
// Created by Ingil Ying on 2022/7/3.
//

#ifndef AWACHAT_MESSAGEPACKAGE_H
#define AWACHAT_MESSAGEPACKAGE_H

#include <QObject>
#include "Message.h"

class MessagePackage : public QObject {
    Q_OBJECT
public:
    MessagePackage(QObject *parent = nullptr);
    static QByteArray LoginPackage(QString user,QString pwd);
    static QByteArray GetInfoPackage();
    static QByteArray ChatPackage(Message* msg,QString target);
    ~MessagePackage();
};


#endif //AWACHAT_MESSAGEPACKAGE_H
