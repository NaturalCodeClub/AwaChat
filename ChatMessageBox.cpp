//
// Created by Ingil Ying on 2022/7/4.
//

#include "ChatMessageBox.h"
#include <QDebug>
#include <QFontMetrics>
#include <vector>

ChatMessageBox::ChatMessageBox(QWidget *parent) : QWidget(parent)
{

}

void ChatMessageBox::setAvatar(QPixmap image)
{
    this->avatar=image;
}

void ChatMessageBox::setOndir(bool ondir)
{
    this->ondir=ondir;
}

void ChatMessageBox::setText(QString text)
{
    this->text=text;
}

void ChatMessageBox::paintEvent(QPaintEvent *event)
{
    Q_UNUSED(event);
    QPainter painter(this);
    painter.setPen(Qt::NoPen);
    painter.setBrush(QBrush(Qt::gray));
    if(ondir)
    {
        QRect avatar_rect(this->width()-5-avatar_wh,4,avatar_wh,avatar_wh);
        painter.drawPixmap(avatar_rect,avatar);
        int str_width,line_height,str_line=1;
        std::vector<QString>lines;
        text_font=this->font();
//        font.setFamily(this->font());
        text_font.setPointSize(15);
        QFontMetrics fm(text_font);
        int str_len=this->text.length();
        int endchar_num=0;
        //qDebug()<<str_len<<Qt::endl;
        while(1)
        {
            for (int i = 0; i < text.length(); ++i) {
                if(text[i]=='\n')
                {
                    text[i]=' ';
                    continue;
                }
            }
            break;
        }
        //int addtion=text.count("？");
        if(text.contains("？"))
            text.replace("？","?");
        if(str_len<=10)
        {
            QRect font_rect=fm.boundingRect(text);
            str_width=font_rect.width();
            line_height=font_rect.height();
            lines.push_back(text);
        }else{
            QRect font_rect=fm.boundingRect(text.left(10));
            str_width=font_rect.width();
            line_height=font_rect.height();
            str_line=str_len/10;
            endchar_num=str_len%10;
            if(endchar_num>0)   str_line+=1;
            int line_start=0;
            int i;
            for(i=0;i<text.length();++i)
            {
                if((i+1-line_start)%10==0){
                    lines.push_back(text.mid(line_start,i+1-line_start));
                    line_start=i+1;
                }
            }
            if((i-line_start)>0)
            {
                lines.push_back(text.mid(line_start,i-line_start));
            }
            str_line=lines.size();
        }
        QRect borer_rect(this->width()-5-avatar_wh-10-str_width-5-10,10,str_width+5+5+10,line_height*str_line+2*(str_line-1)+10);
        painter.setBrush(QBrush(QColor(61,197,255)));
        painter.drawRoundedRect(borer_rect,4,4);
        QRect text_rect(this->width()-5-avatar_wh-5-str_width-5-5,10+3+1,str_width+5,line_height);
        QPen pen_text;
        this->item_height=borer_rect.height()+15;
        pen_text.setColor(Qt::black);
        painter.setPen(pen_text);
        QTextOption textOption(Qt::AlignLeft | Qt::AlignVCenter);
        textOption.setWrapMode(QTextOption::WrapAtWordBoundaryOrAnywhere);
        //painter.setBrush(QBrush(Qt::black));
        painter.setFont(text_font);
        if(str_len<=10)
            painter.drawText(text_rect,text,textOption);
        else{
            for (int i = 0; i < lines.size(); ++i) {
                painter.drawText(QRect(text_rect.x(),10+3+1+line_height*i+3*i,fm.boundingRect(lines[i]).width()+5,line_height),lines[i],textOption);
            }
        }
    }else{
        QRect avatar_rect(5,4,avatar_wh,avatar_wh);
        painter.drawPixmap(avatar_rect,avatar);
        int str_width,line_height,str_line=1;
        std::vector<QString>lines;
        text_font=this->font();
//        font.setFamily(this->font());
        text_font.setPointSize(15);
        QFontMetrics fm(text_font);
        int str_len=this->text.length();
        int endchar_num=0;
        //qDebug()<<str_len<<Qt::endl;
        if(str_len<=10)
        {
            QRect font_rect=fm.boundingRect(text);
            str_width=font_rect.width();
            line_height=font_rect.height();
            lines.push_back(text);
        }else{
            QRect font_rect=fm.boundingRect(text.left(10));
            str_width=font_rect.width();
            line_height=font_rect.height();
            str_line=str_len/10;
            endchar_num=str_len%10;
            if(endchar_num>0)   str_line+=1;
            int line_start=0;
            int i;
            for(i=0;i<text.length();++i)
            {
                if(text[i]=='\n')
                {
                    if(i==line_start)  {
                        i=i+1;
                        line_start=i;
                        continue;
                    }
                    lines.push_back(text.mid(line_start,i-line_start));
                    line_start=i+1;
                }else if((i+1-line_start)%10==0){
                    lines.push_back(text.mid(line_start,i+1-line_start));
                    line_start=i+1;
                }
            }
            if((i-line_start)>0)
            {
                lines.push_back(text.mid(line_start,i-line_start));
            }
            str_line=lines.size();
        }
        QRect borer_rect(5+avatar_wh+5,10,str_width+5+5+10,line_height*str_line+2*(str_line-1)+10);
        painter.setBrush(QBrush(QColor(135,135,135)));
        painter.drawRoundedRect(borer_rect,4,4);
        QRect text_rect(20+avatar_wh,10+3+1,str_width+5,line_height);
        QPen pen_text;
        this->item_height=borer_rect.height()+15;
        pen_text.setColor(Qt::black);
        painter.setPen(pen_text);
        QTextOption textOption(Qt::AlignLeft | Qt::AlignVCenter);
        textOption.setWrapMode(QTextOption::WrapAtWordBoundaryOrAnywhere);
        //painter.setBrush(QBrush(Qt::black));
        painter.setFont(text_font);
        if(str_len<=10)
            painter.drawText(text_rect,text,textOption);
        else{
            for (int i = 0; i < lines.size(); ++i) {
                painter.drawText(QRect(text_rect.x(),10+3+1+line_height*i+3*i,fm.boundingRect(lines[i]).width()+5,line_height),lines[i],textOption);
            }
        }
    }

}

int ChatMessageBox::getItemHeight()
{
    return item_height;
}

void ChatMessageBox::updatedata()
{
    paintEvent(nullptr);
}

void ChatMessageBox::Create(QPixmap image, bool ondir, QString text,QListWidget *list)
{
    ChatMessageBox *box=new ChatMessageBox();
    box->setAvatar(image);
    box->setOndir(ondir);
    box->setText(text);
    QListWidgetItem *item=new QListWidgetItem();
    box->updatedata();
    item->setSizeHint(QSize(list->width()-5,box->getItemHeight()));
    list->addItem(item);
    list->setItemWidget(item,box);
}

ChatMessageBox::~ChatMessageBox()
{

}