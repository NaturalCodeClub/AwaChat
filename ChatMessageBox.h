#ifndef AWACHAT_CHATMESSAGEBOX_H
#define AWACHAT_CHATMESSAGEBOX_H

#include <QWidget>
#include <QPixmap>
#include <QPaintEvent>
#include <QPainter>

class ChatMessageBox  : public QWidget{
    Q_OBJECT
public:
    ChatMessageBox(QWidget *parent= nullptr);
    void setAvatar(QPixmap image);
    //设置消息的方向，true为右，false为左
    void setOndir(bool ondir);
    void setText(QString text);
    int getItemHeight();
    void updatedata();
    ~ChatMessageBox();
private:
    QPixmap avatar;
    bool ondir;
    QString text;
    int avatar_wh=32;
    int item_height;
    QFont text_font;
    QRect avatar_rect;

    void processRect();
protected:
    void paintEvent(QPaintEvent *event);
};


#endif //AWACHAT_CHATMESSAGEBOX_H
