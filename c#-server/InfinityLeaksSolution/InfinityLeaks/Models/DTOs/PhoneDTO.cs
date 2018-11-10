using InfinityLeaks.Models.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace InfinityLeaks.Models.DTOs
{
    public class PhoneDTO
    {
        public int Id { get; set; }
        public byte[] Bytes{ get; set; }

        public int Weight { get; set; }

        public int Width { get; set; }
        public int Height { get; set; }

        public int Thickness { get; set; }

        public int BatteryHours { get; set; }
        public string Cpu { get; set; }
        public int Ram { get; set; }

        public PhoneDTO(int id, byte[] bytes, int weight, int width, int height, int thickness, int batteryHours, string cpu, int ram)
        {
            Id = id;
            Bytes = bytes;
            Weight = weight;
            Width = width;
            Height = height;
            Thickness = thickness;
            BatteryHours = batteryHours;
            Cpu = cpu;
            Ram = ram;
        }

        public PhoneDTO(Phone phone, byte[] bytes)
        {
            Id = phone.Id;
            Bytes = bytes;
            Weight = phone.Weight;
            Width = phone.Width;
            Height = phone.Height;
            Thickness = phone.Thickness;
            BatteryHours = phone.BatteryHours;
            Cpu = phone.Cpu;
            Ram = phone.Ram;
        }

    }
}