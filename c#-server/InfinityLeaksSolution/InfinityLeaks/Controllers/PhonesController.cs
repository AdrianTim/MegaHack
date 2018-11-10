using InfinityLeaks.Models;
using InfinityLeaks.Models.Data;
using InfinityLeaks.Models.DTOs;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web.Http;

namespace InfinityLeaks.Controllers
{
    [RoutePrefix("api/phones")]
    public class PhonesController : ApiController
    {
        public string assetsPath = "D:\\Personal\\MegaHack\\Server\\Assets\\";
        public string connectionString = "Server=localhost;Database=InfinityLeaksDb;Trusted_Connection=True;";

        [Route("{id}/sfb")]
        public byte[] GetSfb(int id)
        {
            return File.ReadAllBytes(assetsPath + id.ToString() + ".sfb");
        }
        
        [Route("{id:int}")]
        public Phone Get([FromUri] int id)
        {
            using (var db = new AppDbContext(connectionString))
            {
                return db.Phones.Find(id);
            }
        }

        [Route("{name}")]
        public Phone Get([FromUri] string name)
        {
            using (var db = new AppDbContext(connectionString))
            {
                return db.Phones.FirstOrDefault(phone => phone.Name == name);
            }
        }
        
        // /api/phones
        [Route("")]
        public IEnumerable<Phone> Get()
        {
            var connectionString = "Server=localhost;Database=InfinityLeaksDb;Trusted_Connection=True;";

            using (var db = new AppDbContext(connectionString))
            {
                //var basePath = "C:\\Users\\rusuc\\source\\repos\\AdrianTim\\MegaHack\\c#-server\\InfinityLeaksSolution\\InfinityLeaks\\Assets\\";
                //var phone = new Phone(1, basePath + "fixphone.sfb", 10, 15, 20, 25, 30, "CPU", 4);
                //db.Phones.Add(phone);

                //db.SaveChanges();

                var query = db.Phones;

                var phones = new List<Phone>();

                foreach(var item in query)
                {
                    //using (var file = new FileStream(item.Path, FileMode.Open, FileAccess.Read))
                    //{
                    //    BinaryReader reader = new BinaryReader(file);

                    //    var a = 

                    //    var bytesCount = new FileInfo(phone.Path).Length;

                    //    var bytes = reader.ReadBytes((int) bytesCount);
                    //}

                    //if (!File.Exists(item.Path))
                    //{
                    //    var directory = System.AppDomain.CurrentDomain.BaseDirectory;
                    //    continue;
                    //}

                    //var sfb = File.ReadAllBytes(item.Path);
                    
                    phones.Add(item);
                    //phones.Add(new PhoneDTO(item, sfb));
                }

                return phones;
            }
        }
    }
}
