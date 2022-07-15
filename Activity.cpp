//
// Created by Ingil Ying on 2022/7/15.
//

#include "Activity.h"


Activity::Activity(QObject *parent)
    : QObject(parent)
{

}

Activity::~Activity()
{

}

void Activity::AddMessage(Message *msg)
{
    this->history.push_back(msg);
}