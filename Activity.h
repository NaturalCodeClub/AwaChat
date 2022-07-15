//
// Created by Ingil Ying on 2022/7/15.
//

#ifndef AWACHAT_ACTIVITY_H
#define AWACHAT_ACTIVITY_H

#include <QObject>
#include <vector>
#include "Message.h"

class Activity : public QObject {
    Q_OBJECT
public:
    Activity(QObject *parent = nullptr);
    ~Activity();
    void AddMessage(Message* msg);
    QString target;
    std::vector<Message*>history;
};


#endif //AWACHAT_ACTIVITY_H
