//
// Created by Ingil Ying on 2022/7/3.
//

#ifndef AWACHAT_MESSAGEPACKAGE_H
#define AWACHAT_MESSAGEPACKAGE_H

#include <QObject>

class MessagePackage : public QObject {
    Q_OBJECT
public:
    MessagePackage(QObject *parent = nullptr);
    static QByteArray LoginPackage(QString user,QString pwd);
    ~MessagePackage();
};


#endif //AWACHAT_MESSAGEPACKAGE_H
