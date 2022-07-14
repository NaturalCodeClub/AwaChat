//
// Created by Ingil Ying on 2022/7/14.
//

#ifndef AWACHAT_MESSAGE_H
#define AWACHAT_MESSAGE_H

#include <QObject>

class Message : QObject{
    Q_OBJECT
public:
    QString text;//The message content
    bool type;//Left : true
            // Right: false
    QString msg_sender;//the message sender's id
    Message(QObject *parent= nullptr);
    static Message* Create(QString msg_sender,bool type,QString text);
    ~Message();
};


#endif //AWACHAT_MESSAGE_H
