using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace InfinityLeaks.Models.Data
{
    public class Phone
    {
        public int Id { get; set; }
        public string Name { get; set; }

        public string Path { get; set; }

        public int Weight { get; set; }

        public int Width { get; set; }
        public int Height { get; set; }

        public int Thickness { get; set; }
        
        public int BatteryHours { get; set; }
        public string Cpu { get; set; }
        public int Ram { get; set; }

        public Phone()
        {

        }

        public Phone(int id, string path, int weight, int width, int height, int thickness, int batteryHours, string cpu, int ram)
        {
            Id = id;
            Path = path;
            Weight = weight;
            Width = width;
            Height = height;
            Thickness = thickness;
            BatteryHours = batteryHours;
            Cpu = cpu;
            Ram = ram;
        }
    }
}