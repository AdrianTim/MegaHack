using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using MessagingToolkit.QRCode.Codec.Data;

namespace QRcoding
{
    class Program
    {
        static void Main(string[] args)
        {
            Bitmap bmp = null;
            
            startEncode(5, bmp);


        }

        public static void startEncode(int id, Bitmap bmp)
        {
            string fileName = "C:\\Users\\chilloutandrew\\Desktop\\exporthtml\\dQRcode_id5.jpeg";
            MessagingToolkit.QRCode.Codec.QRCodeEncoder encoder = new MessagingToolkit.QRCode.Codec.QRCodeEncoder();
            encoder.QRCodeScale = 10;
            bmp = encoder.Encode(id.ToString());
            bmp.Save(fileName, System.Drawing.Imaging.ImageFormat.Jpeg);
        }
 
    }
}
