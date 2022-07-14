/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 6.1.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QLabel>
#include <QtWidgets/QListWidget>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QStackedWidget>
#include <QtWidgets/QTextEdit>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralwidget;
    QListWidget *chat_list;
    QListWidget *modules_list;
    QStackedWidget *stackedWidget;
    QWidget *page;
    QListWidget *avtivities_list;
    QWidget *page_2;
    QListWidget *friends_list;
    QTextEdit *textEdit;
    QPushButton *but_send;
    QLabel *label_target;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QString::fromUtf8("MainWindow"));
        MainWindow->resize(740, 454);
        MainWindow->setMinimumSize(QSize(740, 454));
        MainWindow->setMaximumSize(QSize(740, 454));
        MainWindow->setStyleSheet(QString::fromUtf8("background-color: rgb(47, 47, 47);"));
        centralwidget = new QWidget(MainWindow);
        centralwidget->setObjectName(QString::fromUtf8("centralwidget"));
        centralwidget->setMinimumSize(QSize(738, 454));
        centralwidget->setMaximumSize(QSize(738, 454));
        chat_list = new QListWidget(centralwidget);
        chat_list->setObjectName(QString::fromUtf8("chat_list"));
        chat_list->setGeometry(QRect(190, 30, 531, 291));
        chat_list->setStyleSheet(QString::fromUtf8(" QScrollBar{width:0;height:0};"));
        modules_list = new QListWidget(centralwidget);
        new QListWidgetItem(modules_list);
        new QListWidgetItem(modules_list);
        modules_list->setObjectName(QString::fromUtf8("modules_list"));
        modules_list->setGeometry(QRect(10, 10, 91, 441));
        modules_list->setStyleSheet(QString::fromUtf8("QListWidget{\n"
"	outline: none;\n"
"}\n"
"\n"
"QListWidget::item{\n"
"	background-color: rgb(47, 47, 47);\n"
"	color: rgb(193, 193, 193);\n"
"	border: transparent;\n"
"	padding: 5px;\n"
"}\n"
"\n"
"QListWidget::item:hover{\n"
"	background-color: rgb(89, 89, 89);\n"
"}\n"
"\n"
"QListWidget::item:selected{\n"
"	border-left: 5px solid #009688;\n"
"}"));
        stackedWidget = new QStackedWidget(centralwidget);
        stackedWidget->setObjectName(QString::fromUtf8("stackedWidget"));
        stackedWidget->setGeometry(QRect(90, 10, 91, 441));
        page = new QWidget();
        page->setObjectName(QString::fromUtf8("page"));
        avtivities_list = new QListWidget(page);
        avtivities_list->setObjectName(QString::fromUtf8("avtivities_list"));
        avtivities_list->setGeometry(QRect(0, 0, 91, 441));
        avtivities_list->setStyleSheet(QString::fromUtf8("QListWidget{\n"
"	outline: none;\n"
"}\n"
"\n"
"QListWidget::item{\n"
"	background-color: rgb(47, 47, 47);\n"
"	color: rgb(193, 193, 193);\n"
"	border: transparent;\n"
"	padding: 5px;\n"
"}\n"
"\n"
"QListWidget::item:hover{\n"
"	background-color: rgb(89, 89, 89);\n"
"}\n"
"\n"
"QListWidget::item:selected{\n"
"	background-color: rgb(62, 80, 89);\n"
"}"));
        stackedWidget->addWidget(page);
        page_2 = new QWidget();
        page_2->setObjectName(QString::fromUtf8("page_2"));
        friends_list = new QListWidget(page_2);
        friends_list->setObjectName(QString::fromUtf8("friends_list"));
        friends_list->setGeometry(QRect(0, 0, 91, 441));
        friends_list->setStyleSheet(QString::fromUtf8("QListWidget{\n"
"	outline: none;\n"
"}\n"
"\n"
"QListWidget::item{\n"
"	background-color: rgb(47, 47, 47);\n"
"	color: rgb(193, 193, 193);\n"
"	border: transparent;\n"
"	padding: 5px;\n"
"}\n"
"\n"
"QListWidget::item:hover{\n"
"	background-color: rgb(89, 89, 89);\n"
"}\n"
"\n"
"QListWidget::item:selected{\n"
"	background-color: rgb(62, 80, 89);\n"
"}"));
        stackedWidget->addWidget(page_2);
        textEdit = new QTextEdit(centralwidget);
        textEdit->setObjectName(QString::fromUtf8("textEdit"));
        textEdit->setGeometry(QRect(190, 330, 531, 111));
        textEdit->setStyleSheet(QString::fromUtf8("color: rgb(156, 156, 156);"));
        but_send = new QPushButton(centralwidget);
        but_send->setObjectName(QString::fromUtf8("but_send"));
        but_send->setGeometry(QRect(610, 390, 101, 41));
        but_send->setStyleSheet(QString::fromUtf8("QPushButton{\n"
"	background-color: rgb(15, 203, 255);\n"
"	border-radius: 3px;\n"
"}\n"
"QPushButton:hover{\n"
"	\n"
"	background-color: rgb(205, 245, 255);\n"
"}\n"
"\n"
"QPushButton:pressed{\n"
"	\n"
"	background-color: rgb(10, 134, 168);\n"
"}"));
        label_target = new QLabel(centralwidget);
        label_target->setObjectName(QString::fromUtf8("label_target"));
        label_target->setGeometry(QRect(190, 10, 131, 16));
        label_target->setStyleSheet(QString::fromUtf8("color: rgb(193, 193, 193);"));
        MainWindow->setCentralWidget(centralwidget);
        chat_list->raise();
        modules_list->raise();
        stackedWidget->raise();
        textEdit->raise();
        label_target->raise();
        but_send->raise();

        retranslateUi(MainWindow);

        stackedWidget->setCurrentIndex(0);


        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QCoreApplication::translate("MainWindow", "MainWindow", nullptr));

        const bool __sortingEnabled = modules_list->isSortingEnabled();
        modules_list->setSortingEnabled(false);
        QListWidgetItem *___qlistwidgetitem = modules_list->item(0);
        ___qlistwidgetitem->setText(QCoreApplication::translate("MainWindow", "\344\274\232\350\257\235", nullptr));
        QListWidgetItem *___qlistwidgetitem1 = modules_list->item(1);
        ___qlistwidgetitem1->setText(QCoreApplication::translate("MainWindow", "\351\200\232\350\256\257\345\275\225", nullptr));
        modules_list->setSortingEnabled(__sortingEnabled);

        textEdit->setHtml(QCoreApplication::translate("MainWindow", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n"
"<html><head><meta name=\"qrichtext\" content=\"1\" /><meta charset=\"utf-8\" /><style type=\"text/css\">\n"
"p, li { white-space: pre-wrap; }\n"
"</style></head><body style=\" font-family:'Microsoft YaHei UI'; font-size:9pt; font-weight:400; font-style:normal;\">\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><br /></p></body></html>", nullptr));
        but_send->setText(QCoreApplication::translate("MainWindow", "\345\217\221\351\200\201", nullptr));
        label_target->setText(QString());
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
