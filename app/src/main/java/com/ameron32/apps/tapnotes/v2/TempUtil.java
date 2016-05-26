package com.ameron32.apps.tapnotes.v2;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.parse.Constants;
import com.ameron32.apps.tapnotes.v2.data.parse.Queries;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Note;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Program;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Talk;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by klemeilleur on 5/25/2016.
 */
public class TempUtil {

  static String programId = "7HvHWXHOtz";

  static String[][] notes = new String[][] {
      {"a001", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "", "", "", ""},
      {"a002", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "", "", "", ""},
      {"a003", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<12 11 32 1 <1 Chronicles 12:33>!>> @<<!<18 85 10 <Psalm 86:11>!>>", "", ""},
      {"a004", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<46 9 4 <2 Corinthians 10:5>!>>", "", ""},
      {"a005", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<3 13 1 25 26 <Numbers 14:2, 26, 27>!>>", "", ""},
      {"a006", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<18 36 26 27 <Psalm 37:27, 28>!>>", "", ""},
      {"a007", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "", "", "", ""},
      {"a008", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<1 19 0 1 2 3 4 5 6 <Exodus 20:1-7>!>> @<<!<1 23 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 <Exodus 24:3-18>!>> @<<!<1 31 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 <Exodus 32:1-35>!>> @<<!<1 33 0 1 2 3 4 5 6 7 8 9 10 11 12 13 <Exodus 34:1-14>!>>", "", ""},
      {"a009", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<18 62 2 <Psalm 63:3>!>> @<<!<65 14 3 <Revelation 15:4>!>>", "", ""},
      {"a010", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "", "", "", ""},
      {"a011", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "", "", "", ""},
      {"a012", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "", "", "", ""},
      {"a013", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<43 1 26 <Acts 2:27>!>> @<<!<41 1 50 51 <Luke 2:51, 52>!>>", "", ""},
      {"a014", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<43 1 26 <Acts 2:27>!>> @<<!<59 1 20 21 22 <1 Peter 2:21-23>!>>", "", ""},
      {"a015", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<43 1 26 <Acts 2:27>!>> @<<!<39 3 2 3 4 5 6 7 8 9 <Matthew 4:3-10>!>>", "", ""},
      {"a016", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<43 1 26 <Acts 2:27>!>> @<<!<40 9 16 17 <Mark 10:17, 18>!>>", "", ""},
      {"a017", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<43 1 26 <Acts 2:27>!>> @<<!<40 5 30 31 32 33 <Mark 6:31-34>!>> @<<!<42 3 5 33 <John 4:6, 34>!>>", "", ""},
      {"a018", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<43 1 26 <Acts 2:27>!>> @<<!<42 15 31 <John 16:32>!>>", "", ""},
      {"a019", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<43 1 26 <Acts 2:27>!>> @<<!<57 11 1 <Hebrews 12:2>!>>", "", ""},
      {"a020", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "", "", "", ""},
      {"a021", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<18 96 9 <Psalm 97:10>!>> @<<!<18 118 103 <Psalm 119:104>!>>", "", ""},
      {"a022", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<45 4 10 11 12 <1 Corinthians 5:11-13>!>>", "", ""},
      {"a023", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<18 85 4 <Psalm 86:5>!>> @<<!<39 17 34 <Matthew 18:35>!>>", "", ""},
      {"a024", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<48 3 22 23 <Ephesians 4:23, 24>!>>", "", ""},
      {"a025", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "Scriptures:", "@<<!<57 6 25 <Hebrews 7:26>!>>", "", ""},
      {"a026", "Daily Theme: \"Cherish loyalty\"", "@<<!<32 5 7 <Micah 6:8>!>>", "", "", "", ""},
      {"b001", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "", "", "", ""},
      {"b002", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "", "", "", ""},
      {"b003", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<59 2 0 1 <1 Peter 3:1, 2>!>>", "", ""},
      {"b004", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<43 25 27 28 <Acts 26:28, 29>!>>", "", ""},
      {"b005", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<59 1 24 <1 Peter 2:25>!>>", "", ""},
      {"b006", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<42 0 39 40 41 <John 1:40-42>!>>", "", ""},
      {"b007", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<53 1 13 <1 Timothy 2:14>!>>", "", ""},
      {"b008", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<9 14 1 2 3 4 5 <2 Samuel 15:2-6>!>>", "", ""},
      {"b009", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<10 10 3 4 5 6 7 8 9 <1 Kings 11:4-10>!>>", "", ""},
      {"b010", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<39 25 13 14 15 <Matthew 26:14-16>!>>", "", ""},
      {"b011", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "", "", "", ""},
      {"b012", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<7 0 15 16 <Ruth 1:16, 17>!>>", "", ""},
      {"b013", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<9 22 13 14 15 16 <2 Samuel 23:14-17>!>>", "", ""},
      {"b014", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<9 14 29 30 31 32 33 34 35 36 <2 Samuel 15:30-37>!>>", "", ""},
      {"b015", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<1 0 14 15 16 17 18 19 20 <Exodus 1:15-21>!>>", "", ""},
      {"b016", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<18 88 32 <Psalm 89:33>!>>", "", ""},
      {"b017", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "", "", "", ""},
      {"b018", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "", "", "", ""},
      {"b019", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "", "", "", ""},
      {"b020", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<17 0 5 6 7 8 9 10 11 <Job 1:6-12>!>>", "", ""},
      {"b021", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<17 1 10 <Job 2:11>!>>", "", ""},
      {"b022", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<17 36 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 <Job 37:1-24>!>> @<<!<17 37 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 <Job 38:1-41>!>>", "", ""},
      {"b023", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<17 38 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 <Job 39:1-30>!>> @<<!<17 39 0 1 2 3 4 5 6 7 8 9 10 11 12 13 <Job 40:1-14>!>> @<<!<17 40 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 <Job 41:1-34>!>>", "", ""},
      {"b024", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "", "", "", ""},
      {"b025", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<44 7 24 <Romans 8:25>!>>", "", ""},
      {"b026", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "Scriptures:", "@<<!<46 3 15 16 17 <2 Corinthians 4:16-18>!>>", "", ""},
      {"b027", "Daily Theme: \"Your loyal ones will praise you\"", "@<<!<18 144 9 <Psalm 145:10>!>>", "", "", "", ""},
      {"c001", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "", "", "", ""},
      {"c002", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "", "", "", ""},
      {"c003", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<59 4 4 5 <1 Peter 5:5, 6>!>>", "", ""},
      {"c004", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<47 5 6 7 <Galatians 6:7, 8>!>>", "", ""},
      {"c005", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<19 12 19 <Proverbs 13:20>!>> @<<!<45 14 32 <1 Corinthians 15:33>!>>", "", ""},
      {"c006", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<19 28 24 <Proverbs 29:25>!>>", "", ""},
      {"c007", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<18 115 11 13 <Psalm 116:12, 14>!>>", "", ""},
      {"c008", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<47 4 15 21 22 <Galatians 5:16, 22, 23>!>>", "", ""},
      {"c009", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<45 9 23 <1 Corinthians 10:24>!>> @<<!<50 2 13 <Colossians 3:14>!>>", "", ""},
      {"c010", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<57 10 0 16 17 18 <Hebrews 11:1, 17-19>!>>", "", ""},
      {"c011", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "", "", "", ""},
      {"c012", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<39 4 37 38 39 40 41 42 43 44 <Matthew 5:38-45>!>>", "", ""},
      {"c013", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "", "", "", ""},
      {"c014", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "", "", "", ""},
      {"c015", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "", "", "", ""},
      {"c016", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "", "", "", ""},
      {"c017", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<18 24 0 1 <Psalm 25:1, 2>!>> @<<!<11 15 0 1 2 3 4 5 6 7 8 9 10 11 <2 Kings 16:1-12>!>> @<<!<11 16 0 1 2 3 4 5 6 7 8 9 10 11 12 <2 Kings 17:1-13>!>> @<<!<11 17 0 1 2 3 4 5 6 7 8 9 10 11 12 <2 Kings 18:1-13>!>> @<<!<11 18 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 <2 Kings 19:1-37>!>> @<<!<13 30 0 1 2 3 4 5 6 7 8 9 <2 Chronicles 31:1-10>!>> @<<!<13 31 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 <2 Chronicles 32:1-33>!>>", "", ""},
      {"c018", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "", "", "", ""},
      {"c019", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "Scriptures:", "@<<!<18 3 2 6 7 <Psalm 4:3, 7, 8>!>>", "", ""},
      {"c020", "Daily Theme: \"He is guarding the lives of his loyal ones\"", "@<<!<18 96 9 <Psalm 97:10>!>>", "", "", "", ""},
  };

  static String[][] songs = new String[][] {
      { "a002", "102 Join in the Kingdom Song!\n(Psalm 98:1)\n1. This is a song, a happy song of vict\'ry;\nIt magnifies the One who is supreme.\nThe words give hope and prompt all to be loyal.\nCome sing with us; enjoy its Kingdom theme:\n\'Come worship God Before His throne.\nHis Son is King; Let\'s make it known!\nCome learn this song, this song about the Kingdom;\nBow down to God, and praise His holy name.\'\n\n2. With this new song, we advertise the Kingdom.\nChrist Jesus rules; the earth is his domain.\nAnd as foretold, there is a newborn nation:\nThe Kingdom heirs, who welcome Jesus\' reign:\n\'Come worship God Before His throne.\nHis Son is King; Let\'s make it known!\nCome learn this song, this song about the Kingdom;\nBow down to God, and praise His holy name.\'\n\n3. This Kingdom song, all humble ones can master.\nThe words are clear, their message warm and bright.\nIn all the earth, vast multitudes have learned it,\nAnd they in turn still others now invite:\n\'Come worship God Before His throne.\nHis Son is King; Let\'s make it known!\nCome learn this song, this song about the Kingdom;\nBow down to God, and praise His holy name.\'" },
      { "a007", "66 Serving Jehovah Whole-Souled\n(Matthew 22:37)\n1. O Jehovah, Sov\'reign Ruler,\nYou are the one I love and obey.\nYou deserve my full devotion;\nYour trust in me I shall not betray.\nYour commands I loyally follow;\nAll you wish I gladly will do.\nO Jehovah, you are worthy;\nWhole-souled devotion I give to you.\n\n2. Father, all your works exalt you.\nEarth, moon, and stars your glory proclaim.\nI do give my life to serve you;\nWith all my strength I\'ll make known your name.\nTo my pledge of full dedication,\nI will ever strive to be true.\nO Jehovah, you are worthy;\nWhole-souled devotion I give to you." },
      { "a010", "97 Forward, You Ministers\nof the Kingdom!\n(2 Timothy 4:5)\n1. Go forward in preaching the Kingdom\nTo people in ev\'ry land.\nWith love in your hearts for your neighbor,\nHelp meek ones to take their stand.\nOur service to God is a priv\'lege;\nHis word we are glad to proclaim.\nGo out in the field and keep preaching;\nGive witness to God\'s holy name.\nForward, boldly preach the Kingdom message ever far and wide.\nForward, faithful, loyally remaining on Jehovah\'s side.\n\n2. True ministers keep pressing forward\nWith God\'s prize of life in view.\nWe follow the steps of our Master\nWith hearts that have been made new.\nThe good news of God\'s coming Kingdom\nIs something that all need to hear.\nWe preach in the strength of Jehovah;\nWith him there is nothing to fear!\nForward, boldly preach the Kingdom message ever far and wide.\nForward, faithful, loyally remaining on Jehovah\'s side.\n\n3. Together we press ever forward,\nGod\'s remnant and other sheep.\nThe old and the young men and women\nIn step with the truth do keep.\nOur service we hold to be sacred;\nOur worship is never routine.\nTo God may we prove ever worthy\nBy conduct that\'s holy and clean.\nForward, boldly preach the Kingdom message ever far and wide.\nForward, faithful, loyally remaining on Jehovah\'s side." },
      { "a012", "18 God\'s Loyal Love\n(Isaiah 55:1-3)\n1. Loyal love! God is love.\nThis truth cheers us from above.\nLove caused God to send his Son,\nWho for us the ransom won,\nThat we might gain righteousness,\nLife eternal, happiness.\nHey there, all you thirsty ones,\nCome and drink life\'s water free.\nYes, come drink, you thirsty ones;\nGod\'s loving-kindness see.\n\n2. Loyal love! God is love.\nAll his works give proof thereof.\nLove for us he\'s further shown,\nGiving Christ the kingly throne\nTo fulfill his cov\'nant sworn.\nSee! His Kingdom has been born.\nHey there, all you thirsty ones,\nCome and drink life\'s water free.\nYes, come drink, you thirsty ones;\nGod\'s loving-kindness see.\n\n3. Loyal love! God is love.\nMay his love move us to love.\nLoyally we\'ll help the meek,\nAs God\'s righteous way they seek.\nMay we preach with godly fear,\nComfort spread for all to hear.\nHey there, all you thirsty ones,\nCome and drink life\'s water free.\nYes, come drink, you thirsty ones;\nGod\'s loving-kindness see." },
      { "a020", "29 Walking in Integrity\n(Psalm 26)\n1. Please, judge me, Lord,\nobserve my loyalty;\nObserve my trust in you and my integrity.\nExamine me, and put me to the test;\nMy mind and heart refine,\nthat my soul might be blessed\nBut as for me, Determined I shall be\nto walk eternally In my integrity.\n\n2. I do not sit with wicked men of lies.\nI hate the company of those who truth despise.\nWith evil men, take not away my life;\nMy soul, with those whose hands\nare full of bribes and strife.\nBut as for me, Determined I shall be\nto walk eternally In my integrity.\n\n3. For I have loved the dwelling of your house.\nYour worship, oh, so pure,\nI daily will espouse.\nAnd I will march around your altar grand,\nTo make thanksgiving heard\naloud throughout the land.\nBut as for me, Determined I shall be\nto walk eternally In my integrity." },
      { "a026", "108 Praise Jehovah\nfor His Kingdom\n(Revelation 21:2)\n1. Jehovah anointed his Son\nTo rule over ev\'ryone.\nHis throne is established on justice,\nThat God\'s will on earth may be done.\nPraise Jah for his holy Anointed. Hail Jesus, O you faithful sheep,\nWho loyally follow day after day and all his commandments keep.\nPraise Jah for his holy Anointed, the Ruler of heavenly fame,\nAnointed with exultation and might to honor God\'s holy name.\n\n2. Christ\'s brothers are chosen and called.\nGod gives them their own new birth.\nThis bride class will share in the Kingdom\nAnd bring Paradise to this earth.\nPraise Jah for his holy Anointed. Hail Jesus, O you faithful sheep,\nWho loyally follow day after day and all his commandments keep.\nPraise Jah for his holy Anointed, the Ruler of heavenly fame,\nAnointed with exultation and might to honor God\'s holy name." },
      { "b002", "145 Preparing to Preach\n(Jeremiah 1:17)\n1. Morning comes.\nSoon we will be\nOn our way to preach good news.\nBut it\'s dark outside,\nAnd the rain starts to fall.\nIt would be easy to stay inside,\nsleepy-eyed.\nPositive thoughts and preparation,\nPraying that we\'ll succeed;\nThis can provide the inspiration,\nWe\'ll surely need.\nWe\'re not alone; the angels guide us.\nJesus commands them all.\nAnd with a loyal friend beside us,\nWe\'ll never fall.\n\n2. Soon we\'ll see\nJoy come our way\nIf these things we keep in mind.\nAnd Jehovah sees\nEv\'ry effort we make,\nAnd he remembers the love we show;\nthis we know.\nPositive thoughts and preparation,\nPraying that we\'ll succeed;\nThis can provide the inspiration,\nWe\'ll surely need.\nWe\'re not alone; the angels guide us.\nJesus commands them all.\nAnd with a loyal friend beside us,\nWe\'ll never fall." },
      { "b011", "86 Faithful Women,\nChristian Sisters\n(Romans 16:2)\n1. Sarah and Esther, Mary, Ruth, and others—\nAll these were capable women, loyal wives.\nGodly devotion was foremost in their lives.\nThey were faithful women, ones we know by name.\nThere were others favored by Jehovah,\nNameless in the record, their faith was just the same.\n\n2. Loyalty, courage, goodness, loving-kindness,\nLovable qualities in all humankind,\nVirtues these excellent women bring to mind.\nThey were fine examples all can imitate.\nChristian sisters, walking in their footsteps,\nWorthy is your service and happy your estate.\n\n3. Mothers and daughters, sisters, wives, and widows,\nWillingly laboring, glad to do your part.\nModest your bearing, submissive is your heart,\nHaving God\'s approval, may you never fear.\nChristian sisters, may Jehovah keep you\nFirm in your conviction, your prize is drawing near." },
      { "b017", "7 Christian Dedication\n(Hebrews 10:7, 9)\n1. Because Jehovah created\nThe universe so grand,\nTo him belong the earth and sky,\nThe works of his own hand.\nThe breath of life he has given\nAnd to his creatures shown\nThat worthy is he to have the praise,\nThe worship of all his own.\n\n2. In water Jesus was baptized\nTo righteousness fulfill.\nIn solemn prayer he said to God:\n\'I\'ve come to do your will.\'\nWhen he came up from the Jordan\nAs God\'s anointed Son,\nObedient and loyal he would serve\nAs God\'s dedicated One.\n\n3. We come before you, Jehovah,\nTo praise your name so great.\nDisowning self, with humble hearts\nOur lives we dedicate.\nYou gave your only begotten,\nWho paid the price so high.\nNo longer as living for ourselves,\nFor you we shall live or die." },
      { "b019", "106 Gaining Jehovah\'s Friendship\n(Psalm 15)\n1. Who is your friend, O God?\nWho in your tent may dwell?\nWho gains your friendship? Who gains your trust?\nWho really knows you well?\nAll who embrace your Word,\nAll who have faith in you,\nAll who are loyal, all who are just,\nLiving the truth for you.\n\n2. Who is your friend, O God?\nWho may approach your throne?\nWho brings delight and Makes you rejoice?\nWhose name to you is known?\nAll who exalt your name,\nAll who your Word obey,\nAll who are faithful, honest in heart,\nTruthful in all they say.\n\n3. Rolling our cares on you,\nBaring our hearts in prayer,\nDrawing us closer, bonding in love,\nFeeling your daily care,\nWe yearn to be your friend.\nLong may our friendship grow.\nNo greater Friend could we ever gain,\nNo greater Friend we\'ll know." },
      { "b024", "123 Shepherds—Gifts in Men\n(Ephesians 4:8)\n1. Help in our lives, Jehovah provides,\nShepherds to tend his flock.\nBy their example they serve as guides,\nShowing us how to walk.\nGod gives us men who have earned our trust,\nMen who are loyal and true.\nThey show concern for his precious flock;\nLove them for all that they do.\n\n2. Shepherds who love us care how we feel;\nGently they guide the way.\nWhen we are hurt, they help us to heal,\nKind in the words they say.\nGod gives us men who have earned our trust,\nMen who are loyal and true.\nThey show concern for his precious flock;\nLove them for all that they do.\n\n3. Godly advice and counsel they give,\nThat we may never stray.\nThus they assist us, God\'s way to live,\nServing him ev\'ry day.\nGod gives us men who have earned our trust,\nMen who are loyal and true.\nThey show concern for his precious flock;\nLove them for all that they do." },
      { "b027", "105 The Heavens Declare\nGod\'s Glory\n(Psalm 19)\n1. The heavens tell the glory of Jehovah.\nThe work of his own hand in skies above we see.\nAnd each new day brings to him rightful praise.\nThe starlit night proclaims his might\nAnd his true majesty.\n\n2. Jehovah\'s law is perfect, life-restoring,\nAnd his reminders guide the steps of old and young.\nHis rulings prove to be true, right, and just.\nHis word is sure, his law so pure,\nSo sweet upon the tongue.\n\n3. The fear of God is pure and stands forever.\nThe worth of his commands exceeds the finest gold.\nHis orders lead and preserve all his own.\nHis honor, fame, and holy name,\nWe loyally uphold." },
      { "c002", "61 What Sort of Person\nI Should Be\n(2 Peter 3:11)\n1. How can I repay you, what gift can I give\nTo thank you, Jehovah, for the life that I live?\nI look in my heart with your Word as my mirror;\nThe person I see, may you help me see clearer.\nMy life I have promised in service to you,\nBut not from mere duty will I do what I do.\nWhole-souled and wholehearted I serve you by choice;\nMay I be one more who makes you rejoice.\n\n2. Help me to examine, and help me to see\nJust what sort of person you desire me to be.\nThose loyal to you, you will loyally treasure;\nMay I be among those who bring your heart pleasure." },
      { "c011", "133 Seek God\nfor Your Deliverance\n(Zephaniah 2:3)\n1. Nations align as one,\nOpposing Jehovah\'s Son.\nTheir time of human rulership\nBy God\'s decree now is done.\nRulers have had their day;\nGod\'s Kingdom is here to stay.\nSoon Christ will crush earthly enemies.\nNo more will there be delay.\nSeek God for your deliverance,\nYes, look to him with confidence.\nSeek his righteousness,\nShow your faithfulness,\nFor his sov\'reignty, take your stand.\nThen see our God deliver you\nBy his mighty hand.\n\n2. People on earth now choose,\nResponding to this good news.\nWe offer to all men the choice\nTo hear or proudly refuse.\nTrials, although severe,\nNeed not fill our hearts with fear.\nJehovah cares for his loyal ones;\nOur cries for help he will hear.\nSeek God for your deliverance,\nYes, look to him with confidence.\nSeek his righteousness,\nShow your faithfulness,\nFor his sov\'reignty, take your stand.\nThen see our God deliver you\nBy his mighty hand." },
      { "c014", "125 Loyally Submitting to Theocratic Order\n(1 Corinthians 14:33)\n1. As Jehovah\'s people sound throughout the earth\nTruths about the Kingdom and its priceless worth,\nTheocratic order they must all obey\nAnd remain united, loyalty display.\nLoyal submission in recognition,\nThis to our God we owe.\nHe gives protection, tender affection,\nLoyalty to him we show.\n\n2. God provides his steward and his active force.\nThese will ever guide us in our Christian course.\nSo may we be steadfast, seeking God to please,\nLoyally proclaiming all his wise decrees!\nLoyal submission in recognition,\nThis to our God we owe.\nHe gives protection, tender affection,\nLoyalty to him we show." },
      { "c016", "49 Jehovah Is Our Refuge\n(Psalm 91)\n1. Jehovah is our refuge,\nOur God in whom we trust.\nHis shadow is our shelter;\nAbide in it we must.\nFor he himself will us defend,\nUpon his might we can depend.\nJehovah is a stronghold,\nGiving shelter to all the just.\n\n2. Though thousands will be falling\nAlong your very side,\nAmong those who are loyal,\nIn safety you\'ll reside.\nYou will not need to quake with fear,\nAs though great harm to you were near.\nYour eyes will merely see it,\nUnderneath God\'s wings you\'ll abide.\n\n3. Protection God will give you\nFrom snares along your way,\nAnd never will you falter\nIn fear or in dismay.\nThe maned young lion, you\'ll not dread;\nUpon the cobra you will tread.\nJehovah is our refuge,\nEver guarding us on our way." },
      { "c018", "131 Jehovah\nProvides Escape\n(2 Samuel 22:1-8)\n1. The living God, Jehovah, you have proved to be;\nYour mighty works abound in earth and sky and sea.\nNo rival god can equal what you have done—there is none.\nOur foes will be consumed.\nJehovah provides escape for the loyal.\nHis servants will see what a mighty Crag is he.\nSo with courage and faith in our God, we spread the fame\nOf Jehovah, our Source of escape, and praise his name.\n\n2. Though ropes of death encircle me, I call to you,\n\"Jehovah, give me strength, and give me courage too.\"\nFrom your own temple dwelling, you hear my plea, \"Shelter me;\nRescue me, O my God.\"\nJehovah provides escape for the loyal.\nHis servants will see what a mighty Crag is he.\nSo with courage and faith in our God, we spread the fame\nOf Jehovah, our Source of escape, and praise his name.\n\n3. From heaven you will thunder and give forth your voice.\nYour enemies will quake; your servants will rejoice.\nYou prove to be whatever you need to be; all will see\nHow you provide escape.\nJehovah provides escape for the loyal.\nHis servants will see what a mighty Crag is he.\nSo with courage and faith in our God, we spread the fame\nOf Jehovah, our Source of escape, and praise his name." },
      { "c020", "63 Ever Loyal\n(Psalm 18:25)\n1. Ever loyal to Jehovah,\nLoyal love we wish to show.\nAs a people, dedicated,\nHis commands we want to know.\nHis advice will never fail us,\nAnd his counsel we obey.\nHe is loyal; we can trust him.\nFrom his side we\'ll never stray.\n\n2. Ever loyal to our brothers,\nSticking close in times of need.\nEver caring, always trusting,\nEver kind in word and deed.\nWe show honor to our brothers\nAnd respect them from the heart.\nLet the Bible draw us closer;\nFrom their side we\'ll never part.\n\n3. Ever loyal to their guidance\nWhen our brothers take the lead.\nWhen they give us clear direction,\nMay our mind and heart give heed.\nThen the blessing from Jehovah\nWill be ours to make us strong.\nWhen we\'re loyal, ever faithful,\nTo Jehovah we\'ll belong." },
  };

  public static void deleteTheOld() {
    try {
      DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
      DateTime dt = formatter.parseDateTime("05/26/2016 00:00:00");
      log(dt.toString());

      final List<Note> createdBy = ParseQuery.getQuery(Note.class)
          .whereDoesNotExist("uOwner")
          .whereGreaterThanOrEqualTo(Constants.NOTE_CREATEDAT_DATE_KEY,
              dt.toDate())
          .setLimit(1000)
          .find();

      log("delete #: " + createdBy.size());

      Thread.sleep(30 * 1000);

      Note.deleteAll(createdBy);
      log("deleted");
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void pushNotesA() {

    try {
      for (String[] s2 : notes) {
        String seq = s2[0];
        String note1txt = s2[1] + " " + s2[2];
        String note2txt = s2[3] + " " + s2[4];
        if (note2txt.length() < 2) {
          note2txt = null;
        }

        log("S: " + seq);
        log("n1: " + note1txt);
        if (note2txt == null) {
          log("n2:null");
        } else {
          log("n2: " + note2txt);
        }


        ITalk talk = findTalkSeq(seq);
        Note note, note2;
        note = make(note1txt, talk.getId());
        note.save();
        if (note2txt != null) {
          note2 = make(note2txt, talk.getId());
          note2.save();
        }
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public static void pushNotesB() {
    try {
      for (String[] s2 : songs) {
        String seq = s2[0];
        String note1txt = s2[1];

        log("S: " + seq);
        log("n1: " + note1txt);

        ITalk talk = findTalkSeq(seq);
        Note note;
        note = make(note1txt, talk.getId());
        note.save();
        // FIXME notes are not yet saved
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  static ITalk findTalkSeq(String seq) throws ParseException {
    IProgram program = Queries.Local.getProgram(programId);
    ITalk talk = Queries.Local.getTalkAtSequence(program, seq);
    return talk;
  }

  static Note make(String text, String talkId) {
    Note note = Note.create(text, programId, talkId, null);
    return note;
  }

  static void log(String text) {
    Log.v("Temp", text);
  }
}
